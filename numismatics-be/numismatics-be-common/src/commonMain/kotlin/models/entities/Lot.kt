package ru.numismatics.backend.common.models.entities

import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.id.*

data class Lot(
    val id: LotId = LotId.EMPTY,
    override val name: String = "",
    override val description: String = "",
    override val lock: LockId = LockId.NONE,
    val ownerId: UserId = UserId.EMPTY,
//    val section: Section = Section.EMPTY,
    val sectionId: SectionId = SectionId.EMPTY,
//    val name: String = "",
//    val description: String = "",
    val isCoin: Boolean = true,
    val year: UInt = 0u,
//    val country: Country = Country.EMPTY,
    val countryId: CountryId = CountryId.EMPTY,
    val catalogueNumber: String = "",
    val denomination: String = "",
//    val weight: LotWeight = LotWeight.EMPTY,
    val materialId: MaterialId = MaterialId.EMPTY,
    val weight: Float = 0f,
    val condition: Condition = Condition.UNDEFINED,
    val serialNumber: String = "",
    val quantity: UInt = 1u,
    val photos: Photos = mutableListOf(),
    val marketPrice: MarketPriceHistory = mutableListOf()
) : Entity() {

    override fun isEmpty() = (this == EMPTY)

    override fun deepCopy(name: String, description: String, lock: LockId): Entity = copy(
        name = name,
        description = description,
        lock = lock,
        photos = mutableListOf(),
        marketPrice = mutableListOf()
    ).apply {
        this.photos.addAll(this@Lot.photos)
        this.marketPrice.addAll(this@Lot.marketPrice.map { it.copy() })
        this.setPermissions(this@Lot.getPermissions())
    }

    companion object {
        val EMPTY = Lot()
    }

}

typealias MarketPriceHistory = MutableList<MarketPrice>
typealias Photos = MutableList<Base64String>
