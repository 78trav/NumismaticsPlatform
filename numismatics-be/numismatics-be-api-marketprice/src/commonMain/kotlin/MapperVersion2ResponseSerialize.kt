package ru.numismatics.backend.api.marketprice

import ru.numismatics.backend.api.marketprice.models.MarketPriceResponse
import ru.numismatics.backend.api.v2.mapper.v2Mapper

fun v2ResponseSerialize(obj: MarketPriceResponse) =
    v2Mapper.encodeToString(MarketPriceResponse.serializer(), obj)
