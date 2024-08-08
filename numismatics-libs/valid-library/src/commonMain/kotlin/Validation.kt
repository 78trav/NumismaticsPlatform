package ru.numismatics.platform.libs.validation

sealed interface ValidationResult<T, E>
data class ValidationOk<T, E>(val value: T) : ValidationResult<T, E>
data class ValidationEr<T, E>(val errors: List<E>) : ValidationResult<T, E> {
    constructor(error: E) : this(listOf(error))
}

fun <T, E> ValidationResult<T, E>.getOrExec(default: T, block: (ValidationEr<T, E>) -> Unit = {}): T = when (this) {
    is ValidationOk<T, E> -> this.value
    is ValidationEr<T, E> -> {
        block(this)
        default
    }
}

fun <T, E> ValidationResult<T, E>.getOrNull(block: (ValidationEr<T, E>) -> Unit = {}): T? = when (this) {
    is ValidationOk<T, E> -> this.value
    is ValidationEr<T, E> -> {
        block(this)
        null
    }
}
