package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationOperation() = operation {

    name = "Проверки"

    on { true }

//    on { state == State.RUNNING }

    validationStart()

    validationLotOperation()
    validationReferenceOperation()
    validationMarketPriceOperation()

    validationFinish()
}
