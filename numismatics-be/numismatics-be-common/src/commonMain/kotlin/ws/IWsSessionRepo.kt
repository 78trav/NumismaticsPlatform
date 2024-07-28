package ru.numismatics.backend.common.ws

interface IWsSessionRepo {
    fun add(session: IWsSession)
    fun clearAll()
    fun remove(session: IWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IWsSessionRepo {
            override fun add(session: IWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}