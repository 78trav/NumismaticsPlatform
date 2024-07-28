package ru.numismatics.platform.app.ktor.base

import io.ktor.websocket.*
import ru.numismatics.backend.common.ws.IWsSession

data class KtorWsSession(
    private val session: WebSocketSession
) : IWsSession {
    override suspend fun <T> send(obj: T) {
    }
}