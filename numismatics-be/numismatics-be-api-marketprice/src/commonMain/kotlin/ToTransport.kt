package ru.numismatics.backend.api.marketprice

import ru.numismatics.backend.api.marketprice.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.asString
import ru.numismatics.backend.common.models.exception.UnknownCommand
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal
import ru.numismatics.backend.common.models.core.Error as ErrorInternal

fun NumismaticsPlatformContext.toTransport(): MarketPriceResponse =
    if (
        (command in setOf(Command.WS_INIT, Command.WS_CLOSE)) ||
        ((entityType == EntityType.MARKET_PRICE) && setOf(
            Command.CREATE,
            Command.READ,
            Command.DELETE
        ).contains(command))
    )
        marketPriceToTransport()
    else
        throw UnknownCommand(command, entityType)

fun NumismaticsPlatformContext.marketPriceToTransport() = MarketPriceResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    marketPrice = entityResponse
        .filterIsInstance<Lot>()
        .flatMap { it.marketPrice }
        .map { it.toTransport() }
        .takeIf { it.isNotEmpty() }
)

private fun ErrorInternal.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun MutableList<ErrorInternal>.toTransport(): List<Error>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.FINISHING -> ResponseResult.SUCCESS
    else -> null
}

private fun MarketPriceInternal.toTransport() = MarketPrice(date.asString(), amount)
