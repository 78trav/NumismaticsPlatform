package ru.numismatics.backend.api.v2

import ru.numismatics.backend.api.v2.models.*
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.mappers.*
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.quantity
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.year
import ru.numismatics.platform.libs.validation.getOrExec

fun NumismaticsPlatformContext.fromTransport(request: ILotRequest) {
    requestType = request.debug?.mode?.value.toMode().getOrExec(RequestType.TEST) { er ->
        errors.addAll(er.errors)
        state = State.FAILING
    }
    stubCase = request.debug?.stub?.value.toStubCase().getOrExec(Stubs.NONE) { er ->
        errors.addAll(er.errors)
        state = State.FAILING
    }

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

fun LotCreateObjectV2?.toInternal() =
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
                condition = condition?.value.toCondition().getOrExec(Condition.UNDEFINED),
                serialNumber = serialNumber ?: "",
                quantity = quantity(quantity),
                photos = photos.toBase64StringList(),
                sectionId = sectionId.toSectionId()
            )
        }

fun LotReadObject?.toInternal() = if (this == null) Lot.EMPTY else Lot(id = id.toLotId())

fun LotUpdateObjectV2?.toInternal() =
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
                condition = condition?.value.toCondition().getOrExec(Condition.UNDEFINED),
                serialNumber = serialNumber ?: "",
                quantity = quantity(quantity),
                photos = photos.toBase64StringList(),
                lock = lock.toLockId(),
                sectionId = sectionId.toSectionId()
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

fun LotSearchFilterV2?.toInternal() =
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
                condition = condition?.value.toCondition().getOrExec(Condition.UNDEFINED),
                sectionId = sectionId.toSectionId()
            )
        }
