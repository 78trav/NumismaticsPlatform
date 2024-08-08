package ru.numismatics.backend.common.context

import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.repo.base.IRepo
import ru.numismatics.backend.common.ws.IWsSessionRepo

data class CorSettings<T : Entity>(
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
    val repo: Map<RequestType, IRepo<T>> = emptyMap()
)