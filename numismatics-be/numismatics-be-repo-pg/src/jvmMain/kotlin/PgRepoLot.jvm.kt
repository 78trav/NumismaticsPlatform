package ru.numismatics.backend.repo.pg

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.*
import org.ktorm.support.postgresql.PostgreSqlDialect
import org.ktorm.support.postgresql.insertReturning
import ru.numismatics.backend.common.helpers.toError
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.repo.*
import ru.numismatics.backend.common.repo.base.*
import ru.numismatics.backend.common.repo.exceptions.RepoCommandNotSupport
import ru.numismatics.backend.common.repo.exceptions.RepoEmptyLockException
import ru.numismatics.backend.repo.pg.exceptions.PgDbCommandException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PgRepoLot actual constructor(
    properties: PgProperties,
    private val randomUuid: () -> String
) : RepoBase<Lot>() {

    private val driver = when {
        properties.url.startsWith(DRIVER_PREFIX) -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val db = Database.connect(
        url = properties.url,
        driver = driver,
        user = properties.user,
        password = properties.password,
        dialect = PostgreSqlDialect()
    )

    private fun insert(entity: Lot): IDbEntityResponse {
        val lot = entity.copy(
            lock = randomUuid().toLockId(),
            ownerId = entity.ownerId.takeIf { oid -> oid.isNotEmpty() } ?: randomUuid().toUserId()
        )
        return try {
            db.useTransaction {
                val lotId = when (val id =
                    db.insertReturning(LOTS, LOTS.id) {
                        setFields(lot)
                    }
                ) {
                    is Long -> id.toLotId()
                    else -> LotId.EMPTY
                }
                if (lotId.isEmpty())
                    throw PgDbCommandException("insert")
                else
                    DbEntityResponseSuccess(lot.copy(id = lotId))
            }
        } catch (e: PgDbCommandException) {
            DbEntityResponseError(entity, errorDb(e))
        } catch (e: Exception) {
            DbEntityResponseError(entity, e.toError(code = "db-pg", group = "system"))
        }
    }

    private fun update(entity: Lot): IDbEntityResponse {
        val lot = entity.copy(
            lock = randomUuid().toLockId()
        )
        return try {
            db.useTransaction {
                if (db.update(LOTS) {
                        setFields(lot)
                        where {
                            it.id eq lot.id().toLong()
                        }
                    } == 1) DbEntityResponseSuccess(lot)
                else
                    throw PgDbCommandException("update")
            }
        } catch (e: PgDbCommandException) {
            DbEntityResponseError(entity, errorDb(e))
        }
    }

    private fun read(id: Identifier): IDbEntityResponse =
        db
            .from(LOTS)
            .select()
            .where(LOTS.id.toLong() eq id.toLong())
            .map {
                LOTS.createEntity(it)
            }
            .firstOrNull()?.let {
                DbEntityResponseSuccess(it)
            }
            ?: Lot(id = LotId(id)).let {
                DbEntityResponseError(it, errorNotFound(it))
            }

    private fun delete(lot: Lot): IDbEntityResponse =
        try {
            db.useTransaction {
                if (db.delete(LOTS) { it.id eq lot.id().toLong() } == 1)
                    DbEntityResponseSuccess(lot)
                else
                    throw PgDbCommandException("delete")
            }
        } catch (e: PgDbCommandException) {
            DbEntityResponseError(lot, errorDb(e))
        }

    private fun prepare(
        entity: Lot,
        modifyBlock: (Lot) -> IDbEntityResponse
    ): IDbEntityResponse {
        val id = entity.id()
        if (id == EMPTY_ID)
            return DbEntityResponseError(entity, errorEmptyId())
        else {
            val oldLock = entity.lock
            return if (oldLock.isEmpty())
                DbEntityResponseError(entity, errorEmptyLock(entity))
            else
                when (val res = read(id)) {
                    is DbEntityResponseError<*> -> res
                    is DbEntityResponseSuccess<*> -> {
                        val oldEntity = res.data.first() as Lot
                        when {
                            oldEntity.lock.isEmpty() -> DbEntityResponseError(
                                entity,
                                errorDb(RepoEmptyLockException(id))
                            )

                            oldEntity.lock != oldLock -> DbEntityResponseError(
                                entity,
                                errorRepoConcurrency(oldEntity, oldLock)
                            )

                            else -> modifyBlock(oldEntity)
                        }
                    }

//                    else -> DbEntityResponseError(
//                        entity,
//                        errorDb(PgDbCommandException("read"))
//                    )
                }
        }
    }

    private fun search(filter: Lot): IDbEntityResponse =
        db
            .from(LOTS)
            .select()
            .whereWithConditions {
                if (filter.year > 0U)
                    it += LOTS.year eq filter.year.toShort()
                if (filter.condition != Condition.UNDEFINED)
                    it += LOTS.condition eq filter.condition
                if (filter.countryId.isNotEmpty())
                    it += LOTS.countryId eq filter.countryId.id().toLong()
                if (filter.materialId.isNotEmpty())
                    it += LOTS.materialId eq filter.materialId.id().toLong()
                if (filter.sectionId.isNotEmpty())
                    it += LOTS.sectionId eq filter.sectionId.id().toLong()
            }
            .whereWithOrConditions {
                if (filter.description.isNotEmpty()) {
                    it += LOTS.name like "%${filter.description}%"
                    it += LOTS.description like "%${filter.description}%"
                    it += LOTS.denomination like "%${filter.description}%"
                }
            }
            .map {
                LOTS.createEntity(it)
            }
            .toList()
            .let {
                DbEntityResponseSuccess(it)
            }

    override suspend fun exec(request: DbRequest<Lot>): IDbResponse = tryMethod {
        when (request.command) {
            Command.CREATE -> insert(request.entity)
            Command.READ -> {
                val id = request.entity.id()
                if (id == EMPTY_ID)
                    DbEntityResponseError(request.entity, errorEmptyId())
                else
                    read(id)
            }

            Command.UPDATE -> {
                prepare(request.entity) {
                    val lot = request.entity.copy(
                        lock = randomUuid().toLockId()
                    )
                    update(lot)
                }
            }

            Command.DELETE -> prepare(request.entity) { lot ->
                delete(lot)
            }

            Command.SEARCH -> search(request.entity)
            else -> DbEntityResponseError(
                request.entity,
                RepoCommandNotSupport(request.command, request.entity).toError(
                    code = "repo-wrong-command",
                    group = "repo"
                )
            )
        }
    }

    override fun save(values: Collection<Lot>): Collection<Lot> =
        values
            .map {
                insert(it)
            }
            .filterIsInstance<DbEntityResponseSuccess<*>>()
            .map {
                it.data.first() as Lot
            }


    actual override fun clear() {
        db.deleteAll(LOTS)
    }

    companion object {
        private val LOTS = object : BaseTable<Lot>("lots") {
            val id = long("id").primaryKey()
            val name = varchar("name")
            val description = varchar("description")
            val lock = varchar("lock")
            val ownerId = varchar("owner_id")
            val sectionId = long("section_id")
            val isCoin = boolean("is_coin")
            val year = short("year")
            val countryId = long("country_id")
            val catalogueNumber = varchar("catalogue_number")
            val denomination = varchar("denomination")
            val materialId = long("material_id")
            val weight = float("weight")
            val condition = enum<Condition>("condition")
            val serialNumber = varchar("serial_number")
            val quantity = short("quantity")

            override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Lot(
                id = row[id].toLotId(),
                name = row[name] ?: "",
                description = row[description] ?: "",
                lock = row[lock]?.toLockId() ?: LockId.NONE,
                ownerId = row[ownerId]?.toUserId() ?: UserId.EMPTY,
                sectionId = row[sectionId]?.toSectionId() ?: SectionId.EMPTY,
                isCoin = row[isCoin] ?: true,
                year = (row[year] ?: 0).toUInt(),
                countryId = row[countryId]?.toCountryId() ?: CountryId.EMPTY,
                catalogueNumber = row[catalogueNumber] ?: "",
                denomination = row[denomination] ?: "",
                materialId = row[materialId]?.toMaterialId() ?: MaterialId.EMPTY,
                weight = row[weight] ?: 0f,
                condition = row[condition] ?: Condition.UNDEFINED,
                serialNumber = row[serialNumber] ?: "",
                quantity = (row[quantity] ?: 1).toUInt()
            )

        }

        private fun AssignmentsBuilder.setFields(lot: Lot) {
            set(LOTS.name, lot.name)
            set(LOTS.description, lot.description)
            set(LOTS.lock, lot.lock.asString())
            set(LOTS.ownerId, lot.ownerId.asString())
            set(LOTS.sectionId, lot.sectionId.id().toLong())
            set(LOTS.isCoin, lot.isCoin)
            set(LOTS.year, lot.year.toShort())
            set(LOTS.countryId, lot.countryId.id().toLong())
            set(LOTS.catalogueNumber, lot.catalogueNumber)
            set(LOTS.denomination, lot.denomination)
            set(LOTS.materialId, lot.materialId.id().toLong())
            set(LOTS.weight, lot.weight)
            set(LOTS.condition, lot.condition)
            set(LOTS.serialNumber, lot.serialNumber)
            set(LOTS.quantity, lot.quantity.toShort())
        }
    }
}