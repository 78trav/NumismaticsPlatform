package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.SectionId
import ru.numismatics.backend.common.models.id.toSectionId

data class Section(
    val id: SectionId,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE,
    val parentId: SectionId = SectionId.EMPTY
) : Entity() {

    constructor(id: Long?) : this(id.toSectionId())

    override fun isEmpty() = (this == EMPTY)

    companion object {
        val EMPTY = Section(SectionId.EMPTY)
    }
}