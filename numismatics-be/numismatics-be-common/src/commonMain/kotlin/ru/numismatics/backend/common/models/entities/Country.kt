package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.CountryId

data class Country(
    val id: CountryId,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE
) : Entity() {

    constructor(id: Long?) : this(CountryId.from(id))

    override fun isEmpty() = (this == EMPTY)

    companion object {
        val EMPTY = Country(CountryId.EMPTY)
    }
}
