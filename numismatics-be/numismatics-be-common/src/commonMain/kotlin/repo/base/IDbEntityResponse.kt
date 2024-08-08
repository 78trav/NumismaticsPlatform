package ru.numismatics.backend.common.repo.base

import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.models.core.Error

sealed interface IDbEntityResponse : IDbResponse

data class DbEntityResponseSuccess<T : Entity>(
    val data: List<T> = emptyList()
) : IDbEntityResponse {
    constructor(entity: T) : this(listOf(entity))
}

data class DbEntityResponseError<T : Entity>(
    val data: List<T> = emptyList(),
    val errors: List<Error> = emptyList()
) : IDbEntityResponse {
    constructor(entity: T, error: Error) : this(listOf(entity), listOf(error))
}