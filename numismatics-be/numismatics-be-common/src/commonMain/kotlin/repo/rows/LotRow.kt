package ru.numismatics.backend.common.repo.rows

import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.entities.*
import ru.numismatics.backend.common.models.id.*

data class LotRow(

    override val id: Identifier,
    override val name: String? = null,
    override val description: String? = null,
    override val lock: String? = null,

    val ownerId: String? = null,
    val sectionId: Identifier? = null,
    val isCoin: Boolean? = null,
    val year: UInt? = null,
    val countryId: Identifier? = null,
    val catalogueNumber: String? = null,
    val denomination: String? = null,
    val materialId: Identifier? = null,
    val weight: Float? = null,
    val condition: Condition? = null,
    val serialNumber: String? = null,
    val quantity: UInt? = null

) : RowEntity() {

//    constructor(source: Lot, id: Identifier = EMPTY_ID, lock: String = "") : this(
//        id = if (id == EMPTY_ID) source.id() else id,
//        name = source.name.takeIf { it.isNotBlank() },
//        description = source.description.takeIf { it.isNotBlank() },
//        lock = lock.ifBlank { source.lock.asString() }.takeIf { it.isNotEmpty() },
//
//        ownerId = source.ownerId.asString().takeIf { it.isNotBlank() },
//        sectionId = source.sectionId.takeIf { it.isNotEmpty() }?.id(),
//        isCoin = source.isCoin.takeIf { !it },
//        year = source.year.takeIf { it > 0u },
//        countryId = source.countryId.takeIf { it.isNotEmpty() }?.id(),
//        catalogueNumber = source.catalogueNumber.takeIf { it.isNotBlank() },
//        denomination = source.denomination.takeIf { it.isNotBlank() },
//
//        materialId = source.materialId.takeIf { it.isNotEmpty() }?.id(),
//        weight = source.weight.takeIf { it > 0 },
//
//        condition = source.condition.takeIf { it != Condition.UNDEFINED },
//        serialNumber = source.serialNumber.takeIf { it.isNotBlank() },
//        quantity = source.quantity.takeIf { it > 1u }
//    )

    override fun toEntity(): Lot = Lot(
        id = LotId(id),
        name = name ?: "",
        description = description ?: "",
        lock = lock.toLockId(),
        ownerId = ownerId.toUserId(),
        sectionId = sectionId?.toLong().toSectionId(),
        isCoin = isCoin ?: true,
        year = year ?: 0U,
        countryId = countryId?.toLong().toCountryId(),
        catalogueNumber = catalogueNumber ?: "",
        denomination = denomination ?: "",
        materialId = materialId?.toLong().toMaterialId(),
        weight = weight ?: 0F,
        condition = condition ?: Condition.UNDEFINED,
        serialNumber = serialNumber ?: "",
        quantity = quantity ?: 1U
    )

//    companion object {
//        fun create(source: Entity, id: ULong = 0u, lock: String = "") = when (source) {
//            is Country -> EntityInMemory(source, id, lock)
//            is Material -> EntityInMemory(source, id, lock)
//            is Section -> EntityInMemory(source, id, lock)
//            is Lot -> EntityInMemory(source, id, lock)
//            else -> throw ClassCastException("Can not map ${source::class} to EntityInMemory")
//        }
//    }
}


//fun LotRow.toLot() = Lot(
//    id = LotId(id),
//    name = name ?: "",
//    description = description ?: "",
//    lock = lock.toLockId(),
//    ownerId = ownerId.toUserId(),
//    sectionId = sectionId?.toLong().toSectionId(),
//    isCoin = isCoin ?: true,
//    year = year ?: 0U,
//    countryId = countryId?.toLong().toCountryId(),
//    catalogueNumber = catalogueNumber ?: "",
//    denomination = denomination ?: "",
//    materialId = materialId?.toLong().toMaterialId(),
//    weight = weight ?: 0F,
//    condition = condition ?: Condition.UNDEFINED,
//    serialNumber = serialNumber ?: "",
//    quantity = quantity ?: 1U
//)