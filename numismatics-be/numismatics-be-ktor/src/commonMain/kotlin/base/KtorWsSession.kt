package ru.numismatics.platform.app.ktor.base

import io.ktor.websocket.*
import ru.numismatics.backend.common.ws.IWsSession

data class KtorWsSession(
    private val session: WebSocketSession
) : IWsSession {
    override suspend fun <T> send(obj: T) {
//        require((obj is ILotResponse) || (obj is IReferenceResponse) || (obj is MarketPriceResponse))
//
//        val result = when (obj) {
//            is IReferenceResponse -> if (apiVersion == ApiVersion.V1) v2Serialize(obj) else v2Serialize(obj)
//            is MarketPriceResponse -> if (apiVersion == ApiVersion.V1) v2Serialize(obj) else v2Serialize(obj)
//            is ILotResponse -> if (apiVersion == ApiVersion.V1) v2Serialize(obj) else v2Serialize(obj)
//            else -> ""
//        }
//
//        if (result.isNotEmpty())
//            session.send(Frame.Text(result))
    }
}
