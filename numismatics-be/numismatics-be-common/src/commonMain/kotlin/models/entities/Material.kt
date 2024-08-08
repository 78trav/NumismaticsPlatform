package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.Identifier
import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.MaterialId
import ru.numismatics.backend.common.models.id.toMaterialId
import ru.numismatics.backend.common.repo.rows.RowEntity

data class Material(
    val id: MaterialId,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE,
    val probe: Float = 0f
) : Entity() {

    constructor(id: Long?) : this(id.toMaterialId())

    override fun isEmpty() = (this == EMPTY)

    override fun deepCopy() = copy().apply {
        this.setPermissions(this@Material.getPermissions())
    }

    override fun id() = id.id()

    override fun toRowEntity(id: Identifier, lock: String): RowEntity {
        TODO("Not yet implemented")
    }

    companion object {
        val EMPTY = Material(MaterialId.EMPTY)
    }
}