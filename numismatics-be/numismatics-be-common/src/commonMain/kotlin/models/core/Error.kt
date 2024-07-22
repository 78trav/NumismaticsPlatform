package ru.numismatics.backend.common.models.core

data class Error(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
) {

    companion object {
        val NO_ERROR = Error()
    }
}

typealias Errors = MutableList<Error>

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
