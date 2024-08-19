package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class LockId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = LockId("")
    }
}

fun LockId.isEmpty(): Boolean = (this == LockId.NONE)

fun LockId.isNotEmpty(): Boolean = !isEmpty()

fun String?.toLockId(): LockId = if (this == null) LockId.NONE else LockId(this)
