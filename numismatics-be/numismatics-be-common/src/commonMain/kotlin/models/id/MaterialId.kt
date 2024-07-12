package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class MaterialId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun toLong() = id.toLong()

    companion object {
        val EMPTY = MaterialId(0U)
    }
}

fun MaterialId.isEmpty(): Boolean = (this == MaterialId.EMPTY)

fun MaterialId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toMaterialId(): MaterialId = if (this == null) MaterialId.EMPTY else MaterialId(this.toULong())
