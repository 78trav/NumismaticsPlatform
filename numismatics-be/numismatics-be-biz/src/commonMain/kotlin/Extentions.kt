package ru.numismatics.backend.biz

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext>.start() = job {
    name = "Запуск"

    on { state == State.NONE }

    handle {
        state = State.RUNNING
    }
}
