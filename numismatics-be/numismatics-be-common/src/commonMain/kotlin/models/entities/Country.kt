package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.CountryId
import ru.numismatics.backend.common.models.id.Identifier
import ru.numismatics.backend.common.models.id.toCountryId
import ru.numismatics.backend.common.repo.rows.RowEntity

data class Country(
    val id: CountryId,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE
) : Entity() {

    constructor(id: Long?) : this(id.toCountryId())

    override fun isEmpty() = (this == EMPTY)

    override fun deepCopy(): Entity = copy().apply {
        this@apply.setPermissions(this@Country.getPermissions())
    }

    override fun id(): Identifier = id.id()

    override fun toRowEntity(id: Identifier, lock: String): RowEntity {
        TODO("Not yet implemented")
    }

    companion object {
        val EMPTY = Country(CountryId.EMPTY)
    }
}
