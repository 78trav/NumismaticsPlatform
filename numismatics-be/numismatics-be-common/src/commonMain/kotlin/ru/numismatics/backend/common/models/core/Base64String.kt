package ru.numismatics.backend.common.models.core

import kotlin.jvm.JvmInline

@JvmInline
value class Base64String(private val value: String){
    fun asString() = value

    companion object {
        val EMPTY = Base64String("")
        fun from(id: String?) = if (id == null) EMPTY else Base64String(id)
    }
}
