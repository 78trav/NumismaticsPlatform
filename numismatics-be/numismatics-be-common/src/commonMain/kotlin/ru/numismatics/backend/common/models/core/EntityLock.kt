package ru.numismatics.backend.common.models.core

import kotlin.jvm.JvmInline

@JvmInline
value class EntityLock(private val id: String){
    fun asString() = id

    companion object {
        val NONE = EntityLock("")
        fun from(id: String?) = if (id == null) NONE else EntityLock(id)
    }
}
