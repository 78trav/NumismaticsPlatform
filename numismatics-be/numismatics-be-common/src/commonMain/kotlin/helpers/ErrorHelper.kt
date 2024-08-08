package ru.numismatics.backend.common.helpers

import ru.numismatics.backend.common.models.core.Error

fun Throwable.toError(
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

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, toLong, etc
     */
    violationCode: String,
    description: String
) = Error(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description"
)

inline fun errorSystem(
    violationCode: String,
    exception: Throwable,
) = Error(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    exception = exception
)
