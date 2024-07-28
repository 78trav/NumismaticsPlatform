package ru.numismatics.platform.app.ktor.base

import ru.numismatics.backend.common.ws.IWsSession
import ru.numismatics.backend.common.ws.IWsSessionRepo

class KtorWsSessionRepo : IWsSessionRepo {
    private val sessions: MutableSet<IWsSession> = mutableSetOf()
    override fun add(session: IWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}