package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.LockId

data object EmptyEntity: Entity() {
    override val name: String = ""
    override val description: String = ""
    override val lock: LockId = LockId.NONE
    override fun isEmpty() = true
}
