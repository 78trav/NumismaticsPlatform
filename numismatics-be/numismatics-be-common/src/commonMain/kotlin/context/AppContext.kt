package ru.numismatics.backend.common.context

import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.backend.common.models.entities.Entity

data class AppContext<T : Entity>(
    val apiVersion: ApiVersion,
    val urls: List<String> = emptyList(),
    val corSettings: CorSettings<T> = CorSettings(),
//    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
    val processor: Processor<T>
)