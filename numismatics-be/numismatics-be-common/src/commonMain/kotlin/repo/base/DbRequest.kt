package ru.numismatics.backend.common.repo.base

import ru.numismatics.backend.common.models.core.Command

data class DbRequest<T>(
    val command: Command,
    val entity: T
)