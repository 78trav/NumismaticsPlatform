package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.core.EntityPermission

typealias Permissions = MutableSet<EntityPermission>

sealed class Entity {
    abstract val name: String
    abstract val description: String
    abstract val lock: LockId

    val permissions: Permissions = mutableSetOf()

    abstract fun isEmpty(): Boolean
}

fun <T> Permissions.toTransport(toTransport: (permission: EntityPermission) -> T) =
    this
        .map { toTransport.invoke(it) }
        .toSet()
        .takeIf { it.isNotEmpty() }


typealias Entities = MutableList<Entity>