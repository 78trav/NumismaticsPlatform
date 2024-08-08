package ru.numismatics.backend.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.numismatics.backend.common.helpers.toError
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.entities.*
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.repo.*
import ru.numismatics.backend.common.repo.base.*
import ru.numismatics.backend.common.repo.exceptions.RepoCommandNotSupport
import ru.numismatics.backend.common.repo.exceptions.RepoEmptyLockException
import ru.numismatics.backend.common.repo.rows.LotRow
import ru.numismatics.backend.common.repo.rows.RowEntity
import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class InMemoryRepo<T : Entity>(
    ttl: Duration = 2.minutes,
    initData: Collection<T> = emptyList(),
    private val randomUuid: () -> String = { uuid4().toString() }
) : RepoBase<T>() {

    private val mutex: Mutex = Mutex()

    private val cache = Cache.Builder<Identifier, RowEntity>()
        .expireAfterWrite(ttl)
        .build()

    private var maxId = EMPTY_ID

    private suspend fun prepare(
        entity: T,
        modifyBlock: (Entity) -> IDbEntityResponse
    ): IDbEntityResponse {
        val id = entity.id()
        if (id == EMPTY_ID)
            return DbEntityResponseError(entity, errorEmptyId())
        else {
            val oldLock = entity.lock
            if (oldLock.isEmpty())
                return DbEntityResponseError(entity, errorEmptyLock(entity))
            else
                mutex.withLock {
                    val oldEntity = cache.get(id)?.toEntity()
                    return when {
                        oldEntity == null -> DbEntityResponseError(entity, errorNotFound(entity))
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
        }
    }

    private fun search(filter: Lot): IDbEntityResponse =
        cache.asMap()
            .mapNotNull {
                if (it.value is LotRow) it.value.toEntity() else null
            }
            .filterIsInstance<Lot>()
            .asSequence()
            .filter { lot ->
                filter.description.takeIf { it.isNotBlank() }?.let {
                    lot.name.contains(it, true)
                            || lot.description.contains(it, true)
                            || lot.denomination.contains(it, true)
                }
                    ?: true
            }
            .filter { lot ->
                filter.year.takeIf { it > 0U }?.let { lot.year == it } ?: true
            }
            .filter { lot ->
                filter.condition.takeIf { it != Condition.UNDEFINED }?.let { lot.condition == it }
                    ?: true
            }
            .filter { lot ->
                filter.countryId.takeIf { it.isNotEmpty() }?.let { lot.countryId == it }
                    ?: true
            }
            .filter { lot ->
                filter.materialId.takeIf { it.isNotEmpty() }?.let { lot.materialId == it }
                    ?: true
            }
            .filter { lot ->
                filter.sectionId.takeIf { it.isNotEmpty() }?.let { lot.sectionId == it }
                    ?: true
            }
            .filter { lot ->
                filter.isCoin == lot.isCoin
            }
            .toList()
            .let {
                DbEntityResponseSuccess(it)
            }


    override suspend fun exec(request: DbRequest<T>) = tryMethod {
        when {
            request.command == Command.CREATE ->
                mutex.withLock {
                    request.entity.toRowEntity(id = maxId.inc(), lock = randomUuid()).let {
                        cache.put(it.id, it)
                        DbEntityResponseSuccess(it.toEntity())
                    }
                }

            request.command == Command.READ -> {
                val id = request.entity.id()
                if (id == EMPTY_ID)
                    DbEntityResponseError(request.entity, errorEmptyId())
                else {
                    cache.get(id)
                        ?.let {
                            DbEntityResponseSuccess(it.toEntity())
                        } ?: DbEntityResponseError(request.entity, errorNotFound(request.entity))
                }
            }

            request.command == Command.UPDATE -> prepare(request.entity) {
                request.entity.toRowEntity(lock = randomUuid()).let {
                    cache.put(it.id, it)
                    DbEntityResponseSuccess(it.toEntity())
                }
            }

            request.command == Command.DELETE -> prepare(request.entity) { oldEntity ->
                cache.invalidate(oldEntity.id())
                DbEntityResponseSuccess(oldEntity)
            }

            request.command == Command.SEARCH && request.entity is Lot -> search(request.entity as Lot)

            else -> DbEntityResponseError(
                request.entity,
                RepoCommandNotSupport(request.command, request.entity).toError(
                    code = "repo-wrong-command",
                    group = "repo"
                )
            )
        }
    }

    override fun save(values: Collection<T>): Collection<T> {
        return values
            .filter {
                it.id() > EMPTY_ID //&& it.lock.isNotEmpty()
            }
            .map { entity ->
                entity.toRowEntity().also {
                    cache.put(it.id, it)
                    maxId = max(maxId, it.id)
                }
                entity
            }
    }

    override fun clear() {
        cache.invalidateAll()
    }

    init {
        if (initData.isNotEmpty())
            save(initData)
    }

}