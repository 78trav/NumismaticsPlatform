package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class UserId(private val id: String) {

    fun asString() = id

    companion object {
        val EMPTY = UserId("")
        fun from(id: String?) = if (id == null) EMPTY else UserId(id)
    }
}
