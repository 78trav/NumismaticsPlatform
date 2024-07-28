package ru.numismatics.backend.api.marketprice.test

import kotlinx.datetime.LocalDate
import ru.numismatics.backend.api.marketprice.models.Debug
import ru.numismatics.backend.api.marketprice.models.RequestDebugMode
import ru.numismatics.backend.api.marketprice.models.RequestDebugStubs
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.MarketPrice
import ru.numismatics.backend.common.models.id.*

abstract class TestValues {

    companion object {

        val debug = Debug(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.SUCCESS
        )

        val error = Error(
            code = "err",
            group = "request",
            field = "name",
            message = "wrong name"
        )

        val lotInt = Lot(
            id = LotId(374U),
            marketPrice = mutableListOf(
                MarketPrice(
                    LocalDate.parse("2024-04-07"),
                    4500f
                ),
                MarketPrice(
                    LocalDate.parse("2024-06-07"),
                    5000f
                )
            )
        )

        val filledContext = NumismaticsPlatformContext(
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("5765"),
            entityType = EntityType.MARKET_PRICE,
            entityResponse = mutableListOf(lotInt)
        )

    }
}

