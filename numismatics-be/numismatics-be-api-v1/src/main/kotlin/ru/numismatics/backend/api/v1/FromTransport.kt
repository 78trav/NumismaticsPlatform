package ru.numismatics.backend.api.v1

import ru.numismatics.backend.api.v1.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.mappers.conditionToInternal
import ru.numismatics.backend.common.mappers.modeToInternal
import ru.numismatics.backend.common.mappers.stubCaseToInternal
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.quantity
import ru.numismatics.backend.common.year

fun NumismaticsPlatformContext.fromTransport(request: ILotRequest) {
    requestType = modeToInternal(request.debug?.mode?.value)
    stubCase = stubCaseToInternal(request.debug?.stub?.value)
    entityType = EntityType.LOT

    when (request) {
        is LotCreateRequest -> fromTransport(request)
        is LotReadRequest -> fromTransport(request)
        is LotUpdateRequest -> fromTransport(request)
        is LotDeleteRequest -> fromTransport(request)
        is LotSearchRequest -> fromTransport(request)
    }
}

fun NumismaticsPlatformContext.fromTransport(request: LotCreateRequest) {
    command = Command.CREATE
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotReadRequest) {
    command = Command.READ
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotUpdateRequest) {
    command = Command.UPDATE
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotDeleteRequest) {
    command = Command.DELETE
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: LotSearchRequest) {
    command = Command.SEARCH
    entityRequest = request.filter.toInternal()
}

fun LotCreateObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                name = name ?: "",
                description = description ?: "",
                isCoin = coin ?: true,
                year = year(year),
                countryId = countryId.toCountryId(),
                catalogueNumber = catalogueNumber ?: "",
                denomination = denomination ?: "",
                materialId = materialId.toMaterialId(),
                weight = weight ?: 0f,
                condition = conditionToInternal(condition?.value),
                serialNumber = serialNumber ?: "",
                quantity = quantity(quantity),
                photos = photos.toBase64StringList()
            )
        }

fun LotReadObject?.toInternal() = if (this == null) Lot.EMPTY else Lot(id = id.toLotId())

fun LotUpdateObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                id = id.toLotId(),
                name = name ?: "",
                description = description ?: "",
                isCoin = coin ?: true,
                year = year(year),
                countryId = countryId.toCountryId(),
                catalogueNumber = catalogueNumber ?: "",
                denomination = denomination ?: "",
                materialId = materialId.toMaterialId(),
                weight = weight ?: 0f,
                condition = conditionToInternal(condition?.value),
                serialNumber = serialNumber ?: "",
                quantity = quantity(quantity),
                photos = photos.toBase64StringList(),
                lock = lock.toLockId()
            )
        }

fun LotDeleteObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                id = id.toLotId(),
                lock = lock.toLockId()
            )
        }

fun LotSearchFilter?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                name = name ?: "",
                description = description ?: "",
                isCoin = coin ?: true,
                year = year(year),
                countryId = countryId.toCountryId(),
                denomination = denomination ?: "",
                materialId = materialId.toMaterialId(),
                condition = conditionToInternal(condition?.value),
            )
        }

