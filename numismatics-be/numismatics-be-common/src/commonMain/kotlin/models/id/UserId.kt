package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class UserId(private val id: String) {

    fun asString() = id

    companion object {
        val EMPTY = UserId("")
    }
}

fun UserId.isEmpty(): Boolean = (this == UserId.EMPTY)

fun UserId.isNotEmpty(): Boolean = !isEmpty()

fun String?.toUserId(): UserId = if (this == null) UserId.EMPTY else UserId(this)
