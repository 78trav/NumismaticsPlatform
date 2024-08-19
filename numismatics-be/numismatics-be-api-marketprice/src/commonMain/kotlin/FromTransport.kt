package ru.numismatics.backend.api.marketprice

import ru.numismatics.backend.api.marketprice.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.mappers.modeToInternal
import ru.numismatics.backend.common.mappers.stubCaseToInternal
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.toLocalDateNP
import ru.numismatics.backend.common.models.id.toLotId
import kotlin.math.max
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal

fun NumismaticsPlatformContext.fromTransport(request: IMarketPriceRequest) {
    requestType = modeToInternal(request.debug?.mode?.value)
    stubCase = stubCaseToInternal(request.debug?.stub?.value)
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
        MarketPriceInternal(date, max(this?.amount ?: 0f, 0f))
    }
