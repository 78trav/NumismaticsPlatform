package ru.numismatics.backend.common.repo.base

import ru.numismatics.backend.common.models.entities.Entity

interface IRepo<T : Entity> {
    suspend fun exec(request: DbRequest<T>): IDbResponse
    fun save(values: Collection<T>): Collection<T>
    fun clear()
}