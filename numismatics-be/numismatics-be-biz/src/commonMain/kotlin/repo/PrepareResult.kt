package ru.numismatics.backend.biz.repo

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.prepareResult(name: String) = job {
    this.name = name
    description = "Подготовка данных для ответа клиенту на запрос"
    on { true }
    handle {
        entityResponse.addAll(entityRepoDone)
        if (state == State.RUNNING)
            state = State.FINISHING
    }
}