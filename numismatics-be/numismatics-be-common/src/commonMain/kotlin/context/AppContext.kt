package ru.numismatics.backend.common.context

import ru.numismatics.backend.common.models.core.ApiVersion

data class AppContext(
    val apiVersion: ApiVersion,
    val urls: List<String> = emptyList(),
    val corSettings: CorSettings = CorSettings(),
//    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
    val processor: Processor<NumismaticsPlatformContext>
)
//) : IAppSettings
