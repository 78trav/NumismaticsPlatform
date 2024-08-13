package ru.numismatics.backend.api.v1

import ru.numismatics.backend.api.v1.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Entities
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.asString
import ru.numismatics.backend.common.models.entities.toTransport
import ru.numismatics.backend.common.models.exception.UnknownCommand
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.models.core.Condition as ConditionInternal
import ru.numismatics.backend.common.models.core.EntityPermission as PermissionInternal
import ru.numismatics.backend.common.models.core.Error as ErrorInternal
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal

fun NumismaticsPlatformContext.toTransport(): IResponse = when (entityType) {
    EntityType.LOT ->
        when (command) {
            Command.CREATE -> lotCreateToTransport()
            Command.READ -> lotReadToTransport()
            Command.UPDATE -> lotUpdateToTransport()
            Command.DELETE -> lotDeleteToTransport()
            Command.SEARCH -> lotsToTransport()
            else -> throw UnknownCommand(command, entityType)
        }

    EntityType.MARKET_PRICE ->
        if (setOf(Command.CREATE, Command.READ, Command.DELETE).contains(command))
            marketPriceToTransport()
        else
            throw UnknownCommand(command, entityType)

    else -> throw UnknownCommand(command, entityType)
}

fun NumismaticsPlatformContext.marketPriceToTransport() = MarketPriceResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    marketPrice = entityResponse
        .filterIsInstance<Lot>()
        .flatMap { it.marketPrice }
        .map { it.toTransport() }
)

fun NumismaticsPlatformContext.lotCreateToTransport() = LotCreateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    lot = entityResponse.lotToTransport()
)

fun NumismaticsPlatformContext.lotReadToTransport() = LotReadResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    lot = entityResponse.lotToTransport()
)

fun NumismaticsPlatformContext.lotUpdateToTransport() = LotUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    lot = entityResponse.lotToTransport()
)

fun NumismaticsPlatformContext.lotDeleteToTransport() = LotDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    lot = entityResponse.lotToTransport()
)

fun NumismaticsPlatformContext.lotsToTransport() = LotSearchResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    lots = entityResponse
        .filterIsInstance<Lot>()
        .map { it.toTransport() }
)

private fun Entities.lotToTransport() = this
    .filterIsInstance<Lot>()
    .first()
    .toTransport()

private fun Lot.toTransport() = LotResponse(
    id = id.takeIf { it != LotId.EMPTY }?.toLong(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    isCoin = isCoin,
    year = year.toInt(),
    catalogueNumber = catalogueNumber.takeIf { it.isNotBlank() },
    denomination = denomination.takeIf { it.isNotBlank() },
    weight = takeIf { (weight > 0) || (materialId != MaterialId.EMPTY) }?.let {
        LotWeight(
            weight,
            Material(materialId.toLong())
        )
    },
    condition = condition.toTransport(),
    serialNumber = serialNumber.takeIf { it.isNotBlank() },
    quantity = quantity.toInt(),
    photos = photos.map { it.asString() }.takeIf { it.isNotEmpty() },
    ownerId = ownerId.takeIf { it != UserId.EMPTY }?.asString(),
    country = takeIf { countryId != CountryId.EMPTY }?.let { Country(countryId.toLong()) },
    marketPrice = marketPrice.map { MarketPrice(it.date.asString(), it.amount) }.takeIf { it.isNotEmpty() },
    permissions = permissions.toTransport { it.toTransport() },
    lock = lock.takeIf { it != LockId.NONE }?.asString()
)

private fun ConditionInternal.toTransport() = when (this) {
    ConditionInternal.PF -> Condition.PF
    ConditionInternal.PL -> Condition.PL
    ConditionInternal.BU -> Condition.BU
    ConditionInternal.UNC -> Condition.UNC
    ConditionInternal.AU_PLUS -> Condition.AU_PLUS
    ConditionInternal.AU -> Condition.AU
    ConditionInternal.XF_PLUS -> Condition.XF_PLUS
    ConditionInternal.XF -> Condition.XF
    ConditionInternal.VF_PLUS -> Condition.VF_PLUS
    ConditionInternal.VF -> Condition.VF
    ConditionInternal.F -> Condition.F
    ConditionInternal.VG -> Condition.VG
    ConditionInternal.G -> Condition.G
    ConditionInternal.AG -> Condition.AG
    ConditionInternal.FA -> Condition.FA
    ConditionInternal.PR -> Condition.PR
    else -> null
}

fun PermissionInternal.toTransport() = when (this) {
    PermissionInternal.CREATE -> EntityPermission.CREATE
    PermissionInternal.READ -> EntityPermission.READ
    PermissionInternal.UPDATE -> EntityPermission.UPDATE
    PermissionInternal.DELETE -> EntityPermission.DELETE
}

private fun MutableList<ErrorInternal>.toTransport(): List<Error>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ErrorInternal.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.FINISHING -> ResponseResult.SUCCESS
    else -> null
}

private fun MarketPriceInternal.toTransport() = MarketPrice(date.asString(), amount)
