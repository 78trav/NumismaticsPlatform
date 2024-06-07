package ru.numismatics.backend.api.v2

import ru.numismatics.backend.api.v2.models.*
import ru.numismatics.backend.api.v2.models.Condition
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.stubs.Stubs
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.toLocalDate
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.quantity
import ru.numismatics.backend.common.year
import kotlin.math.max
import ru.numismatics.backend.common.models.core.Condition as ConditionInternal
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal

fun NumismaticsPlatformContext.fromTransport(request: IRequest) {
    requestType = request.debug.toRequestType()
    stubCase = request.debug.toStubCase()

    when (request) {
        is LotCreateRequest -> fromTransport(request)
        is LotReadRequest -> fromTransport(request)
        is LotUpdateRequest -> fromTransport(request)
        is LotDeleteRequest -> fromTransport(request)
        is LotSearchRequest -> fromTransport(request)
        is MarketPriceCreateRequest -> fromTransport(request)
        is MarketPriceDeleteRequest -> fromTransport(request)
        is MarketPriceReadRequest -> fromTransport(request)
    }
}

fun NumismaticsPlatformContext.fromTransport(request: LotCreateRequest) {
    command = Command.CREATE
    entityType = EntityType.LOT
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotReadRequest) {
    command = Command.READ
    entityType = EntityType.LOT
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotUpdateRequest) {
    command = Command.UPDATE
    entityType = EntityType.LOT
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotDeleteRequest) {
    command = Command.DELETE
    entityType = EntityType.LOT
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotSearchRequest) {
    command = Command.SEARCH
    entityType = EntityType.LOT
    entityRequest = request.filter.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: MarketPriceCreateRequest) {
    command = Command.CREATE
    entityType = EntityType.MARKET_PRICE
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: MarketPriceDeleteRequest) {
    command = Command.DELETE
    entityType = EntityType.MARKET_PRICE
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: MarketPriceReadRequest) {
    command = Command.READ
    entityType = EntityType.MARKET_PRICE
    entityRequest = request.lot.toInternal()
}

fun LotCreateObjectV2?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                name = name ?: "",
                description = description ?: "",
                isCoin = isCoin ?: true,
                year = year(year),
                countryId = CountryId.from(countryId),
                catalogueNumber = catalogueNumber ?: "",
                denomination = denomination ?: "",
                materialId = MaterialId.from(materialId),
                weight = weight ?: 0f,
                condition = condition.toInternal(),
                serialNumber = serialNumber ?: "",
                quantity = quantity(quantity),
                marketPrice = marketPrice.toInternal()?.let { mutableListOf(it) } ?: mutableListOf(),
                photos = photos.toBase64StringList(),
                sectionId = SectionId.from(sectionId)
            )
        }

fun LotReadObject?.toInternal() = if (this == null) Lot.EMPTY else Lot(id = LotId.from(id))

fun LotUpdateObjectV2?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                id = LotId.from(id),
                name = name ?: "",
                description = description ?: "",
                isCoin = isCoin ?: true,
                year = year(year),
                countryId = CountryId.from(countryId),
                catalogueNumber = catalogueNumber ?: "",
                denomination = denomination ?: "",
                materialId = MaterialId.from(materialId),
                weight = weight ?: 0f,
                condition = condition.toInternal(),
                serialNumber = serialNumber ?: "",
                quantity = quantity(quantity),
                photos = photos.toBase64StringList(),
                lock = LockId.from(lock),
                sectionId = SectionId.from(sectionId)
            )
        }

fun LotDeleteObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                id = LotId.from(id),
                lock = LockId.from(lock)
            )
        }

fun LotSearchFilterV2?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                name = name ?: "",
                description = description ?: "",
                isCoin = isCoin ?: true,
                year = year(year),
                countryId = CountryId.from(countryId),
                denomination = denomination ?: "",
                materialId = MaterialId.from(materialId),
                condition = condition.toInternal(),
                sectionId = SectionId.from(sectionId)
            )
        }

fun MarketPriceCreateObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                id = LotId.from(id),
                marketPrice = marketPrice.toInternal()?.let { mutableListOf(it) } ?: mutableListOf()
            )
        }

fun MarketPriceDeleteObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            val date = date.toLocalDate()
            Lot(
                id = LotId.from(id),
                marketPrice = if (date == null) mutableListOf() else mutableListOf(
                    ru.numismatics.backend.common.models.entities.MarketPrice(
                        date,
                        0f
                    )
                )
            )
        }

fun MarketPriceReadObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                id = LotId.from(id)
            )
        }

private fun Debug?.toRequestType() = when (this?.mode) {
    RequestDebugMode.PROD -> RequestType.PROD
    RequestDebugMode.TEST -> RequestType.TEST
    RequestDebugMode.STUB -> RequestType.STUB
    else -> RequestType.PROD
}

private fun Debug?.toStubCase() = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    RequestDebugStubs.BAD_NAME -> Stubs.BAD_NAME
    RequestDebugStubs.BAD_DESCRIPTION -> Stubs.BAD_DESCRIPTION
    RequestDebugStubs.BAD_VISIBILITY -> Stubs.BAD_VISIBILITY
    RequestDebugStubs.CANNOT_CREATE -> Stubs.CANNOT_CREATE
    RequestDebugStubs.CANNOT_UPDATE -> Stubs.CANNOT_UPDATE
    RequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    RequestDebugStubs.BAD_SEARCH -> Stubs.BAD_SEARCH
    else -> Stubs.NONE
}


private fun Condition?.toInternal() = when (this) {
    Condition.PF -> ConditionInternal.PF
    Condition.PL -> ConditionInternal.PL
    Condition.BU -> ConditionInternal.BU
    Condition.UNC -> ConditionInternal.UNC
    Condition.AU_PLUS -> ConditionInternal.AU_PLUS
    Condition.AU -> ConditionInternal.AU
    Condition.XF_PLUS -> ConditionInternal.XF_PLUS
    Condition.XF -> ConditionInternal.XF
    Condition.VF_PLUS -> ConditionInternal.VF_PLUS
    Condition.VF -> ConditionInternal.VF
    Condition.F -> ConditionInternal.F
    Condition.VG -> ConditionInternal.VG
    Condition.G -> ConditionInternal.G
    Condition.AG -> ConditionInternal.AG
    Condition.FA -> ConditionInternal.FA
    Condition.PR -> ConditionInternal.PR
    else -> ConditionInternal.UNDEFINED
}

private fun MarketPrice?.toInternal(): MarketPriceInternal? =
    this?.date.toLocalDate()?.let { date ->
        MarketPriceInternal(date, max(this?.amount ?: 0f, 0f))
    }

private fun List<String>?.toBase64StringList() = (
        this?.map { Base64String.from(it) }?.filter { it != Base64String.EMPTY } ?: listOf()
        ).toMutableList()
