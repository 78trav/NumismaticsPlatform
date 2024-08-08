package ru.numismatics.backend.common.repo.rows

import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.models.id.Identifier

abstract class RowEntity {
    abstract val id: Identifier
    abstract val name: String?
    abstract val description: String?
    abstract val lock: String?

    abstract fun toEntity(): Entity
}