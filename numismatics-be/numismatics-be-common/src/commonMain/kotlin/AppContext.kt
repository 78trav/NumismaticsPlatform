package ru.numismatics.backend.common

import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.backend.common.ws.IWsSessionRepo

data class AppContext(
    val apiVersion: ApiVersion,
    val urls: List<String> = emptyList(),
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
    val processor: IProcessor
)
//) : IAppSettings
