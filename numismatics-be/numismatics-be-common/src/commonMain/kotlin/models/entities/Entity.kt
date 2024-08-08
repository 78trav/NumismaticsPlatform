package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.core.EntityPermission
import ru.numismatics.backend.common.models.id.EMPTY_ID
import ru.numismatics.backend.common.models.id.Identifier
import ru.numismatics.backend.common.repo.rows.RowEntity

typealias Permissions = MutableSet<EntityPermission>

abstract class Entity {
    abstract val name: String
    abstract val description: String
    abstract val lock: LockId

    private val permissions: Permissions = mutableSetOf()

    abstract fun isEmpty(): Boolean

    abstract fun deepCopy(): Entity

    abstract fun id(): Identifier

    fun setPermissions(newPermissions: Set<EntityPermission>) {
        permissions.clear()
        permissions.addAll(newPermissions)
    }

    fun getPermissions() = permissions.toSet()

    abstract fun toRowEntity(id: Identifier = EMPTY_ID, lock: String = ""): RowEntity
}

fun <T> Set<EntityPermission>.toTransport(toTransport: (permission: EntityPermission) -> T) =
    this
        .map { toTransport.invoke(it) }
        .toSet()
        .takeIf { it.isNotEmpty() }