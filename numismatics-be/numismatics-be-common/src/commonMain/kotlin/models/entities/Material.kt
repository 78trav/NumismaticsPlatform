package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.MaterialId
import ru.numismatics.backend.common.models.id.toMaterialId

data class Material(
    val id: MaterialId,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE,
    val probe: Float = 0f
) : Entity() {

    constructor(id: Long?) : this(id.toMaterialId())

    override fun isEmpty() = (this == EMPTY)

    override fun deepCopy(name: String, description: String, lock: LockId): Entity = copy(
        name = name,
        description = description,
        lock = lock
    ).apply {
        this.setPermissions(this@Material.getPermissions())
    }

    companion object {
        val EMPTY = Material(MaterialId.EMPTY)
    }
}
