package ru.numismatics.backend.biz.stubs

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext>.stubMarketPriceJob() = job {
    name = "Работа с рыночной ценой"
    description = "Имитация успешной обработки"

    on {
        state == State.RUNNING &&
                requestType == RequestType.STUB &&
                stubCase == Stubs.SUCCESS &&
                entityType == EntityType.MARKET_PRICE &&
                StubProcessor.entitiesCommands[entityType]?.contains(command) ?: false
    }

    handle {
        state = State.FINISHING
        entityResponse.add(StubProcessor.lots.first())
    }
}
