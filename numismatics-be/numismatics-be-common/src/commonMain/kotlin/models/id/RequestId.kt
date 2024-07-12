package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class RequestId(private val id: String) {

    fun asString() = id

    companion object {
        val EMPTY = RequestId("")
    }
}

fun RequestId.isEmpty(): Boolean = (this == RequestId.EMPTY)

fun RequestId.isNotEmpty(): Boolean = !isEmpty()

fun String?.toRequestId(): RequestId = if (this == null) RequestId.EMPTY else RequestId(this)
