package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class RequestId(private val id: String) {

    fun asString() = id

    companion object {
        val EMPTY = RequestId("")
        fun from(id: String?) = if (id == null) EMPTY else RequestId(id)
    }
}
