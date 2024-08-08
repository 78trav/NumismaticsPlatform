package ru.numismatics.backend.repo.pg

//import io.github.moreirasantos.pgkn.PostgresDriver
//import io.github.moreirasantos.pgkn.resultset.ResultSet
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import ru.numismatics.backend.common.helpers.toError
import ru.numismatics.backend.common.mappers.toCondition
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.repo.*
import ru.numismatics.backend.common.repo.base.*
import ru.numismatics.backend.common.repo.exceptions.RepoCommandNotSupport
import ru.numismatics.backend.common.repo.exceptions.RepoEmptyLockException
import ru.numismatics.platform.libs.validation.getOrExec
import libpq.*
import ru.numismatics.backend.repo.pg.exceptions.PgNativeDbException

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
actual class PgRepoLot actual constructor(
    properties: PgProperties,
    private val randomUuid: () -> String
) : RepoBase<Lot>() {

    init {
        val regex = Regex("^[\\w\\d_]+\$")
        val msg = "PostgreSQL database must contain only letters, numbers and underscore symbol '_'"

        require(properties.database.matches(regex)) {
            msg
        }
        require(properties.schema.matches(regex)) {
            msg
        }
        require(properties.table.matches(regex)) {
            msg
        }

        initConnection(properties)
    }

    private val table: String = "\"${properties.schema}\".\"${properties.table}\""

    private suspend fun pqExec(sql: String, command: String): IDbEntityResponse =
        if (PQstatus(connection) == ConnStatusType.CONNECTION_OK) {
            withContext(Dispatchers.IO) {
                val res = PQexec(connection, sql)
                val status = PQresultStatus(res)
                val result = if (status == PGRES_TUPLES_OK) {
                    val lots = mutableListOf<Lot>()
                    for (i in 0..<PQntuples(res))
                        lots.add(res!!.toLot(i))
                    DbEntityResponseSuccess(lots.toList())
                } else null
                PQclear(res)
                result ?: throw PgNativeDbException("DB error (native): $command statement returned $status")
            }
        } else
            throw PgNativeDbException("DB error (native): not connected.")

    private suspend fun insert(entity: Lot): IDbEntityResponse {
        val lot = entity.copy(
            lock = randomUuid().toLockId(),
            ownerId = entity.ownerId.takeIf { oid -> oid.isNotEmpty() } ?: randomUuid().toUserId()
        )
//        :${fields.NAME},
//        :${fields.DESCR},
//        :${fields.LOCK},
//        :${fields.OWNER},
//        :${fields.SECTION},
//        :${fields.COIN},
//        :${fields.YEAR},
//        :${fields.COUNTRY},
//        :${fields.C_NUMBER},
//        :${fields.NOMINAL},
//        :${fields.MATERIAL},
//        :${fields.WEIGHT},
//        :${fields.CONDITION}::${fields.CONDITION_TYPE},
//        :${fields.S_NUMBER},
//        :${fields.QUANTITY}
        val sql = """
            INSERT INTO $table
            (
                ${fields.NAME.quoted()}, 
                ${fields.DESCR.quoted()},
                ${fields.LOCK.quoted()},
                ${fields.OWNER.quoted()},
                ${fields.SECTION.quoted()},
                ${fields.COIN.quoted()},
                ${fields.YEAR.quoted()},
                ${fields.COUNTRY.quoted()},
                ${fields.C_NUMBER.quoted()},
                ${fields.NOMINAL.quoted()},
                ${fields.MATERIAL.quoted()},
                ${fields.WEIGHT.quoted()},
                ${fields.CONDITION.quoted()},
                ${fields.S_NUMBER.quoted()},
                ${fields.QUANTITY.quoted()}
            )
            VALUES
            (
                '${lot.name}',
                '${lot.description}',
                '${lot.lock.asString()}',
                '${lot.ownerId.asString()}',
                ${lot.sectionId.id()},
                ${lot.isCoin},
                ${lot.year},
                ${lot.countryId.id()},
                '${lot.catalogueNumber}',
                '${lot.denomination}',
                ${lot.materialId.id()},
                ${lot.weight},
                '${lot.condition}'::${fields.CONDITION_TYPE},
                '${lot.serialNumber}',
                ${lot.quantity}
            )
            RETURNING
                ${fields.ID}, 
                ${fields.NAME}, 
                ${fields.DESCR},
                ${fields.LOCK},
                ${fields.OWNER},
                ${fields.SECTION},
                ${fields.COIN},
                ${fields.YEAR},
                ${fields.COUNTRY},
                ${fields.C_NUMBER},
                ${fields.NOMINAL},
                ${fields.MATERIAL},
                ${fields.WEIGHT},
                ${fields.CONDITION},
                ${fields.S_NUMBER},
                ${fields.QUANTITY}
            """.trimIndent()
        return try {
            pqExec(sql, "insert")
//            val rows = driver.execute(
//                sql = sql,
//                entity.toDb(),
//            ) { row: ResultSet -> row.toLot() }
//            DbEntityResponseSuccess(rows.first())
        } catch (e: Exception) {
            DbEntityResponseError(entity, e.toError(code = "db-pg", group = "system"))
        }
    }

    private suspend fun update(entity: Lot): IDbEntityResponse {
        val lot = entity.copy(
            lock = randomUuid().toLockId()
        )
//        ${fields.NAME.quoted()} = :${fields.NAME},
//        ${fields.DESCR.quoted()} = :${fields.DESCR},
//        ${fields.LOCK.quoted()} = :${fields.LOCK},
//        ${fields.OWNER.quoted()} = :${fields.OWNER},
//        ${fields.SECTION.quoted()} = :${fields.SECTION},
//        ${fields.COIN.quoted()} = :${fields.COIN},
//        ${fields.YEAR.quoted()} = :${fields.YEAR},
//        ${fields.COUNTRY.quoted()} = :${fields.COUNTRY},
//        ${fields.C_NUMBER.quoted()} = :${fields.C_NUMBER},
//        ${fields.NOMINAL.quoted()} = :${fields.NOMINAL},
//        ${fields.MATERIAL.quoted()} = :${fields.MATERIAL},
//        ${fields.WEIGHT.quoted()} = :${fields.WEIGHT},
//        ${fields.CONDITION.quoted()} = :${fields.CONDITION}::${fields.CONDITION_TYPE},
//        ${fields.S_NUMBER.quoted()} = :${fields.S_NUMBER},
//        ${fields.QUANTITY.quoted()} = :${fields.QUANTITY}
//        WHERE
//        ${fields.ID.quoted()} = :${fields.ID}
        val sql = """
            UPDATE $table
            SET
                ${fields.NAME.quoted()} = '${lot.name}', 
                ${fields.DESCR.quoted()} = '${lot.description}',
                ${fields.LOCK.quoted()} = '${lot.lock.asString()}',
                ${fields.OWNER.quoted()} = '${lot.ownerId.asString()}',
                ${fields.SECTION.quoted()} = ${lot.sectionId.id()},
                ${fields.COIN.quoted()} = ${lot.isCoin},
                ${fields.YEAR.quoted()} = ${lot.year},
                ${fields.COUNTRY.quoted()} = ${lot.countryId.id()},
                ${fields.C_NUMBER.quoted()} = '${lot.catalogueNumber}',
                ${fields.NOMINAL.quoted()} = '${lot.denomination}',
                ${fields.MATERIAL.quoted()} = ${lot.materialId.id()},
                ${fields.WEIGHT.quoted()} = ${fields.WEIGHT},
                ${fields.CONDITION.quoted()} = '${lot.condition}'::${fields.CONDITION_TYPE},
                ${fields.S_NUMBER.quoted()} = '${lot.serialNumber}',
                ${fields.QUANTITY.quoted()} = ${lot.quantity}
            WHERE
                ${fields.ID.quoted()} = ${lot.id()}
            RETURNING
                ${fields.ID}, 
                ${fields.NAME}, 
                ${fields.DESCR},
                ${fields.LOCK},
                ${fields.OWNER},
                ${fields.SECTION},
                ${fields.COIN},
                ${fields.YEAR},
                ${fields.COUNTRY},
                ${fields.C_NUMBER},
                ${fields.NOMINAL},
                ${fields.MATERIAL},
                ${fields.WEIGHT},
                ${fields.CONDITION},
                ${fields.S_NUMBER},
                ${fields.QUANTITY}
            """.trimIndent()
        return try {
            pqExec(sql, "update")
//            val rows = driver.execute(
//                sql = sql,
//                entity.toDb(),
//            ) { row: ResultSet -> row.toLot() }
//            DbEntityResponseSuccess(rows.first())
        } catch (e: Exception) {
            DbEntityResponseError(entity, e.toError(code = "db-pg", group = "system"))
        }
    }

    private suspend fun read(id: Identifier): IDbEntityResponse {
        val lot = Lot(id = LotId(id))
//        WHERE
//        ${fields.ID.quoted()} = :${fields.ID}
        val sql = """
            SELECT
                ${fields.ID.quoted()}, 
                ${fields.NAME.quoted()}, 
                ${fields.DESCR.quoted()},
                ${fields.LOCK.quoted()},
                ${fields.OWNER.quoted()},
                ${fields.SECTION.quoted()},
                ${fields.COIN.quoted()},
                ${fields.YEAR.quoted()},
                ${fields.COUNTRY.quoted()},
                ${fields.C_NUMBER.quoted()},
                ${fields.NOMINAL.quoted()},
                ${fields.MATERIAL.quoted()},
                ${fields.WEIGHT.quoted()},
                ${fields.CONDITION.quoted()},
                ${fields.S_NUMBER.quoted()},
                ${fields.QUANTITY.quoted()}
            FROM
                $table
            WHERE
                ${fields.ID.quoted()} = $id
            """.trimIndent()
        return try {
            when (val result = pqExec(sql, "read")) {
                is DbEntityResponseSuccess<*> -> if (result.data.isEmpty()) DbEntityResponseError(
                    lot,
                    errorNotFound(lot)
                ) else result

                else -> result
            }
//            val rows = driver.execute(
//                sql = sql,
//                mapOf(
//                    fields.ID to id
//                )
//            ) { row: ResultSet -> row.toLot() }
//            if (rows.isEmpty())
//                DbEntityResponseError(lot, errorNotFound(lot))
//            else
//                DbEntityResponseSuccess(rows.first())
        } catch (e: Exception) {
            DbEntityResponseError(lot, e.toError(code = "db-pg", group = "system"))
        }
    }

    private suspend fun delete(lot: Lot): IDbEntityResponse {
//        WHERE
//        ${fields.ID.quoted()} = :${fields.ID}
        val sql = """
            DELETE
            FROM
                $table
            WHERE
                ${fields.ID.quoted()} = ${lot.id()}
            RETURNING
                ${fields.ID}, 
                ${fields.NAME}, 
                ${fields.DESCR},
                ${fields.LOCK},
                ${fields.OWNER},
                ${fields.SECTION},
                ${fields.COIN},
                ${fields.YEAR},
                ${fields.COUNTRY},
                ${fields.C_NUMBER},
                ${fields.NOMINAL},
                ${fields.MATERIAL},
                ${fields.WEIGHT},
                ${fields.CONDITION},
                ${fields.S_NUMBER},
                ${fields.QUANTITY}
            """.trimIndent()
        return try {
            pqExec(sql, "delete")
//            val rows = driver.execute(
//                sql = sql,
//                mapOf(
//                    fields.ID to lot.id()
//                )
//            ) { row: ResultSet -> row.toLot() }
//            if (rows.isEmpty())
//                DbEntityResponseError(lot, errorNotFound(lot))
//            else
//                DbEntityResponseSuccess(rows.first())
        } catch (e: Exception) {
            DbEntityResponseError(lot, e.toError(code = "db-pg", group = "system"))
        }
    }

    private suspend fun prepare(
        entity: Lot,
        modifyBlock: suspend () -> IDbEntityResponse
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

                            else -> modifyBlock()
                        }
                    }
                }
        }
    }

    private suspend fun search(filter: Lot): IDbEntityResponse {
//        val where = listOfNotNull(
//            "${fields.COIN.quoted()} = :${fields.COIN}",
//            filter.description.takeIf { it.isNotBlank() }
//                ?.let {
//                    """
//                        (
//                            ${fields.NAME.quoted()} LIKE :${fields.DESCR} OR
//                            ${fields.DESCR.quoted()} LIKE :${fields.DESCR} OR
//                            ${fields.NOMINAL.quoted()} LIKE :${fields.DESCR}
//                        )
//                        """.trimIndent()
//                },
//            filter.year.takeIf { it > 0U }
//                ?.let {
//                    "${fields.YEAR.quoted()} = :${fields.YEAR}"
//                },
//            filter.condition.takeIf { it != Condition.UNDEFINED }
//                ?.let {
//                    "${fields.CONDITION.quoted()} = :${fields.CONDITION}::${fields.CONDITION_TYPE}"
//                },
//            filter.countryId.takeIf { it.isNotEmpty() }
//                ?.let {
//                    "${fields.COUNTRY.quoted()} = :${fields.COUNTRY}"
//                },
//            filter.materialId.takeIf { it.isNotEmpty() }
//                ?.let {
//                    "${fields.MATERIAL.quoted()} = :${fields.MATERIAL}"
//                },
//            filter.sectionId.takeIf { it.isNotEmpty() }
//                ?.let {
//                    "${fields.SECTION.quoted()} = :${fields.SECTION}"
//                }
//        ).joinToString(separator = " AND ")

        val where = listOfNotNull(
            "${fields.COIN.quoted()} = ${filter.isCoin}",
            filter.description.takeIf { it.isNotBlank() }
                ?.let {
                    """
                        (
                            ${fields.NAME.quoted()} LIKE '%${filter.description}%' OR
                            ${fields.DESCR.quoted()} LIKE '%${filter.description}%' OR                        
                            ${fields.NOMINAL.quoted()} LIKE '%${filter.description}%'                        
                        )
                        """.trimIndent()
                },
            filter.year.takeIf { it > 0U }
                ?.let {
                    "${fields.YEAR.quoted()} = ${filter.year}"
                },
            filter.condition.takeIf { it != Condition.UNDEFINED }
                ?.let {
                    "${fields.CONDITION.quoted()} = '${filter.condition}'::${fields.CONDITION_TYPE}"
                },
            filter.countryId.takeIf { it.isNotEmpty() }
                ?.let {
                    "${fields.COUNTRY.quoted()} = ${filter.countryId.id()}"
                },
            filter.materialId.takeIf { it.isNotEmpty() }
                ?.let {
                    "${fields.MATERIAL.quoted()} = ${filter.materialId.id()}"
                },
            filter.sectionId.takeIf { it.isNotEmpty() }
                ?.let {
                    "${fields.SECTION.quoted()} = ${filter.sectionId.id()}"
                }
        ).joinToString(separator = " AND ")


        val sql = """
            SELECT
                ${fields.ID.quoted()}, 
                ${fields.NAME.quoted()}, 
                ${fields.DESCR.quoted()},
                ${fields.LOCK.quoted()},
                ${fields.OWNER.quoted()},
                ${fields.SECTION.quoted()},
                ${fields.COIN.quoted()},
                ${fields.YEAR.quoted()},
                ${fields.COUNTRY.quoted()},
                ${fields.C_NUMBER.quoted()},
                ${fields.NOMINAL.quoted()},
                ${fields.MATERIAL.quoted()},
                ${fields.WEIGHT.quoted()},
                ${fields.CONDITION.quoted()},
                ${fields.S_NUMBER.quoted()},
                ${fields.QUANTITY.quoted()}
            FROM
                $table
            WHERE
                $where
            """.trimIndent()
        println("SQL: $sql")

        return try {
            pqExec(sql, "search")
//            val rows = driver.execute(
//                sql = sql,
//                filter.toDb()
//            ) { row: ResultSet -> row.toLot() }
//            DbEntityResponseSuccess(rows)
        } catch (e: Exception) {
            DbEntityResponseError(filter, e.toError(code = "db-pg", group = "system"))
        }
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

            Command.UPDATE -> prepare(request.entity) {
                update(request.entity)
            }

            Command.DELETE -> prepare(request.entity) {
                delete(request.entity)
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

    override fun save(values: Collection<Lot>): Collection<Lot> = runBlocking(Dispatchers.IO) {
        values
            .map {
                insert(it)
            }
            .filterIsInstance<DbEntityResponseSuccess<*>>()
            .map {
                it.data.first() as Lot
            }
    }

    actual override fun clear(): Unit = runBlocking(Dispatchers.IO) {
        if (PQstatus(connection) == ConnStatusType.CONNECTION_OK) {
            val res = PQexec(connection, "TRUNCATE TABLE $table")
            if (PQresultStatus(res) == PGRES_COMMAND_OK) {
                println("$table cleared!")
            }
        }
//        driver.execute(sql = "TRUNCATE TABLE $table")
    }

    companion object {
        private var connection: CPointer<PGconn>? = null

        private fun initConnection(properties: PgProperties) {
            if (connection == null) {
                PQinitOpenSSL(0, 0)
                connection =
                    PQconnectdb("host=${properties.host} port=${properties.port} user=${properties.user} password=${properties.password} dbname=${properties.database}")
                println(PQstatus(connection))
            }
        }

//        private lateinit var driver: PostgresDriver
//        private fun initConnection(properties: PgProperties) {
//            if (!this::driver.isInitialized) {
//                driver = PostgresDriver(
//                    host = properties.host,
//                    port = properties.port,
//                    user = properties.user,
//                    database = properties.database,
//                    password = properties.password
//                )
//            }
//        }

        private val fields = object {
            val ID = "id"
            val NAME = "name"
            val DESCR = "description"
            val LOCK = "lock"
            val OWNER = "owner_id"
            val SECTION = "section_id"
            val COIN = "is_coin"
            val YEAR = "year"
            val COUNTRY = "country_id"
            val C_NUMBER = "catalogue_number"
            val NOMINAL = "denomination"
            val MATERIAL = "material_id"
            val WEIGHT = "weight"
            val CONDITION = "condition"
            val S_NUMBER = "serial_number"
            val QUANTITY = "quantity"

            val CONDITION_TYPE = "np_condition_type"

        }

        private fun String.quoted() = "\"$this\""

//        private fun String.toDb() = this.takeIf { it.isNotBlank() }

        private fun Lot.toDb() = mapOf(
            fields.ID to this.id.id(),
            fields.NAME to this.name,
            fields.DESCR to this.description,
            fields.LOCK to this.lock.asString(),
            fields.OWNER to this.ownerId.asString(),
            fields.SECTION to this.sectionId.id(),
            fields.COIN to this.isCoin,
            fields.YEAR to this.year.toShort(),
            fields.COUNTRY to this.countryId.id(),
            fields.C_NUMBER to this.catalogueNumber,
            fields.NOMINAL to this.denomination,
            fields.MATERIAL to this.materialId.id(),
            fields.WEIGHT to this.weight,
            fields.CONDITION to this.condition.name,
            fields.S_NUMBER to this.serialNumber,
            fields.QUANTITY to this.quantity.toShort()
        )

        private fun CPointer<PGresult>.toLot(tupNum: Int) = Lot(
            id = PQgetvalue(this, tupNum, 0)?.toKString()?.toLong().toLotId(),
            name = PQgetvalue(this, tupNum, 1)?.toKString() ?: "",
            description = PQgetvalue(this, tupNum, 2)?.toKString() ?: "",
            lock = PQgetvalue(this, tupNum, 3)?.toKString().toLockId(),
            ownerId = PQgetvalue(this, tupNum, 4)?.toKString().toUserId(),
            sectionId = PQgetvalue(this, tupNum, 5)?.toKString()?.toLong().toSectionId(),
            isCoin = (PQgetvalue(this, tupNum, 6)?.toKString() ?: "t") == "t",
            year = PQgetvalue(this, tupNum, 7)?.toKString()?.toUInt() ?: 0U,
            countryId = PQgetvalue(this, tupNum, 8)?.toKString()?.toLong().toCountryId(),
            catalogueNumber = PQgetvalue(this, tupNum, 9)?.toKString() ?: "",
            denomination = PQgetvalue(this, tupNum, 10)?.toKString() ?: "",
            materialId = PQgetvalue(this, tupNum, 11)?.toKString()?.toLong().toMaterialId(),
            weight = PQgetvalue(this, tupNum, 12)?.toKString()?.toFloat() ?: 0f,
            condition = PQgetvalue(this, tupNum, 13)?.toKString().toCondition().getOrExec(Condition.UNDEFINED),
            serialNumber = PQgetvalue(this, tupNum, 14)?.toKString() ?: "",
            quantity = PQgetvalue(this, tupNum, 15)?.toKString()?.toUInt() ?: 0U
        )


//        private fun ResultSet.toLot() = Lot(
//            id = this.getLong(0).toLotId(),
//            name = this.getString(1) ?: "",
//            description = this.getString(2) ?: "",
//            lock = this.getString(3).toLockId(),
//            ownerId = this.getString(4).toUserId(),
//            sectionId = this.getLong(5).toSectionId(),
//            isCoin = this.getBoolean(6) ?: true,
//            year = (this.getShort(7) ?: 0).toUInt(),
//            countryId = this.getLong(8).toCountryId(),
//            catalogueNumber = this.getString(9) ?: "",
//            denomination = this.getString(10) ?: "",
//            materialId = this.getLong(11).toMaterialId(),
//            weight = this.getFloat(12) ?: 0f,
//            condition = this.getString(13).toCondition().getOrExec(Condition.UNDEFINED),
//            serialNumber = this.getString(14) ?: "",
//            quantity = (this.getShort(15) ?: 0).toUInt()
//        )
    }
}
