package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.repo.rows.LotRow

data class Lot(
    val id: LotId = LotId.EMPTY,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE,
    val ownerId: UserId = UserId.EMPTY,
    val sectionId: SectionId = SectionId.EMPTY,
    val isCoin: Boolean = true,
    val year: UInt = 0u,
    val countryId: CountryId = CountryId.EMPTY,
    val catalogueNumber: String = "",
    val denomination: String = "",
    val materialId: MaterialId = MaterialId.EMPTY,
    val weight: Float = 0f,
    val condition: Condition = Condition.UNDEFINED,
    val serialNumber: String = "",
    val quantity: UInt = 1u
) : Entity() {

    override fun isEmpty() = (this == EMPTY)

    override fun deepCopy() = copy().apply {
        this@apply.setPermissions(this@Lot.getPermissions())
    }

    override fun id(): Identifier = id.id()

    override fun toRowEntity(id: Identifier, lock: String) = LotRow(
        id = if (id == EMPTY_ID) id() else id,
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        lock = lock.ifBlank { this.lock.asString() }.takeIf { it.isNotEmpty() },

        ownerId = ownerId.asString().takeIf { it.isNotBlank() },
        sectionId = sectionId.takeIf { it.isNotEmpty() }?.id(),
        isCoin = isCoin.takeIf { !it },
        year = year.takeIf { it > 0u },
        countryId = countryId.takeIf { it.isNotEmpty() }?.id(),
        catalogueNumber = catalogueNumber.takeIf { it.isNotBlank() },
        denomination = denomination.takeIf { it.isNotBlank() },

        materialId = materialId.takeIf { it.isNotEmpty() }?.id(),
        weight = weight.takeIf { it > 0 },

        condition = condition.takeIf { it != Condition.UNDEFINED },
        serialNumber = serialNumber.takeIf { it.isNotBlank() },
        quantity = quantity.takeIf { it > 1u }
    )

    companion object {
        val EMPTY = Lot()
    }
}