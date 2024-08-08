package ru.numismatics.backend.biz.stubs

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.context.wrongCommand
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal inline fun <reified T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.stubFailJob() = job {

    name = "Команда не поддерживается"
    this.description = "Проверка ситуации, когда команда не обработана на предыдущих этапах"

    on { state == State.RUNNING }

    handle {
        fail(wrongCommand())
    }

}
