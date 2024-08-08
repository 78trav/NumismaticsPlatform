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