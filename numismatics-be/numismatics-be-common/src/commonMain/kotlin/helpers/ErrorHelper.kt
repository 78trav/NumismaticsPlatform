package ru.numismatics.backend.common.helpers

import ru.numismatics.backend.common.models.core.Error

fun Throwable.asError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = Error(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this
)