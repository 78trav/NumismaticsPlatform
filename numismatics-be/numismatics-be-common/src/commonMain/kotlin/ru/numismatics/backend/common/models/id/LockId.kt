package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class LockId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = LockId("")
        fun from(id: String?) = if (id == null) NONE else LockId(id)
    }
}
