package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.start() = job {
    name = "Запуск"
    on { state == State.NONE }
    handle {
        state = State.RUNNING
    }
}

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.validationFinish() = job {
    name = "Завершение проверок"
    on { state == State.RUNNING }
    handle {
        entityValidated = entityValidating
    }
}
