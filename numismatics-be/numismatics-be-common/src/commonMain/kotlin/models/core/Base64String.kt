package ru.numismatics.backend.common.models.core

import kotlin.jvm.JvmInline

@JvmInline
value class Base64String(private val value: String) {
    fun asString() = value

    companion object {
        val EMPTY = Base64String("")
    }
}


fun Base64String.isEmpty(): Boolean = (this == Base64String.EMPTY)

fun Base64String.isNotEmpty(): Boolean = !isEmpty()

fun String?.toBase64String(): Base64String = if (this == null) Base64String.EMPTY else Base64String(this)

fun List<String>?.toBase64StringList() = (
        this?.filter { it.isNotEmpty() }?.map { it.toBase64String() } ?: listOf()
        ).toMutableList()
