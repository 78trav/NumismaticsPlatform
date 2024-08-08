package ru.numismatics.backend.common.models.id

import kotlin.jvm.JvmInline

@JvmInline
value class MaterialId(private val id: Identifier) {

    fun asString(): String = id.toString()

    fun id() = id

    companion object {
        val EMPTY = MaterialId(EMPTY_ID)
    }
}

fun MaterialId.isEmpty(): Boolean = (this == MaterialId.EMPTY)

fun MaterialId.isNotEmpty(): Boolean = !isEmpty()

fun Long?.toMaterialId() = MaterialId(this?.toULong() ?: EMPTY_ID)
