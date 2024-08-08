package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.Identifier
import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.SectionId
import ru.numismatics.backend.common.models.id.toSectionId
import ru.numismatics.backend.common.repo.rows.RowEntity

data class Section(
    val id: SectionId,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE,
    val parentId: SectionId = SectionId.EMPTY
) : Entity() {

    constructor(id: Long?) : this(id.toSectionId())

    override fun isEmpty() = (this == EMPTY)

    override fun deepCopy() = copy().apply {
        this.setPermissions(this@Section.getPermissions())
    }

    override fun id() = id.id()

    override fun toRowEntity(id: Identifier, lock: String): RowEntity {
        TODO("Not yet implemented")
    }

    companion object {
        val EMPTY = Section(SectionId.EMPTY)
    }
}
