package ru.numismatics.backend.api.v1

import ru.numismatics.backend.api.v1.models.*
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.mappers.*
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.quantity
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.year
import ru.numismatics.platform.libs.validation.ValidationEr
import ru.numismatics.platform.libs.validation.ValidationOk
import ru.numismatics.platform.libs.validation.ValidationResult
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
    entityRequest = request.lot.toInternal().getOrExec(Lot.EMPTY) { er ->
        errors.addAll(er.errors)
        state = State.FAILING
    }
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

fun LotCreateObject?.toInternal(): ValidationResult<Lot, Error> =
    if (this == null)
        ValidationOk(Lot.EMPTY)
    else {
        with(this) {
            val errors = mutableListOf<Error>()
            if ((year ?: 0) < 0)
                errors.add(
                    Error(
                        code = "wrong-year",
                        group = "mapper-validation",
                        field = "year",
                        message = "Value of year must be greater than 0"
                    )
                )
            val condition = condition?.value.toCondition().getOrExec(Condition.UNDEFINED) { er ->
                errors.addAll(er.errors)
            }
            if (errors.isEmpty())
                ValidationOk(
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
                        condition = condition,
                        serialNumber = serialNumber ?: "",
                        quantity = quantity(quantity),
                        photos = photos.toBase64StringList()
                    )
                )
            else
                ValidationEr(errors)
        }
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
                condition = condition?.value.toCondition().getOrExec(Condition.UNDEFINED),
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
                condition = condition?.value.toCondition().getOrExec(Condition.UNDEFINED)
            )
        }
