package ru.numismatics.backend.api.marketprice

import ru.numismatics.backend.api.marketprice.models.*
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.mappers.toMode
import ru.numismatics.backend.common.mappers.toStubCase
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.toLocalDateNP
import ru.numismatics.backend.common.models.id.toLotId
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.platform.libs.validation.getOrExec
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal

fun NumismaticsPlatformContext.fromTransport(request: IMarketPriceRequest) {

    requestType = request.debug?.mode?.value.toMode().getOrExec(RequestType.TEST) { er ->
        errors.addAll(er.errors)
        state = State.FAILING
    }
    stubCase = request.debug?.stub?.value.toStubCase().getOrExec(Stubs.NONE) { er ->
        errors.addAll(er.errors)
        state = State.FAILING
    }
    entityType = EntityType.MARKET_PRICE

    when (request) {
        is MarketPriceCreateRequest -> fromTransport(request)
        is MarketPriceDeleteRequest -> fromTransport(request)
        is MarketPriceReadRequest -> fromTransport(request)
    }
}

fun NumismaticsPlatformContext.fromTransport(request: MarketPriceCreateRequest) {
    command = Command.CREATE
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: MarketPriceDeleteRequest) {
    command = Command.DELETE
    entityRequest = request.lot.toInternal()
}

fun NumismaticsPlatformContext.fromTransport(request: MarketPriceReadRequest) {
    command = Command.READ
    entityRequest = request.lot.toInternal()
}

fun MarketPriceCreateObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            Lot(
                id = id.toLotId(),
                marketPrice = marketPrice.toInternal()?.let { mutableListOf(it) } ?: mutableListOf()
            )
        }

fun MarketPriceDeleteObject?.toInternal() =
    if (this == null) Lot.EMPTY else
        with(this) {
            val date = date.toLocalDateNP()
            Lot(
                id = id.toLotId(),
                marketPrice = if (date == null) mutableListOf() else mutableListOf(MarketPriceInternal(date, 0f))
            )
        }

fun MarketPriceReadObject?.toInternal() = if (this == null) Lot.EMPTY else Lot(id = id.toLotId())

private fun MarketPrice?.toInternal(): MarketPriceInternal? =
    this?.date.toLocalDateNP()?.let { date ->
        MarketPriceInternal(date, (this?.amount ?: 0).toFloat())
    }
