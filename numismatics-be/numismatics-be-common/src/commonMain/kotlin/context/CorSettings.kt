package ru.numismatics.backend.common.context

import ru.numismatics.backend.common.ws.IWsSessionRepo

data class CorSettings(
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE
)
