package ru.numismatics.backend.biz

import ru.numismatics.backend.biz.exec.lotCommandExecutionOperation
import ru.numismatics.backend.biz.repo.initRepo
import ru.numismatics.backend.biz.repo.prepareResult
import ru.numismatics.backend.biz.stubs.*
import ru.numismatics.backend.biz.validation.lotValidationOperation
import ru.numismatics.backend.biz.validation.start
import ru.numismatics.backend.common.context.CorSettings
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.Processor
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.platform.libs.cor.operation.process

class BizProcessor(corSettings: CorSettings<Lot>) : Processor<Lot>(corSettings) {

    override suspend fun exec(context: NumismaticsPlatformContext<Lot>) {
        processes[context.requestType == RequestType.STUB]?.exec(
            context.also {
                it.corSettings = corSettings
//                println(context.requestType)
//                println(it)
            }
        )
    }

    private val processes = mapOf(
        true to process<NumismaticsPlatformContext<Lot>> {
            start()
            stubLotOperation()
            stubOtherOperation()
        }.build(),

        false to process<NumismaticsPlatformContext<Lot>> {
            start()
            initRepo("Инициализация репозитория")
            lotValidationOperation()
            lotCommandExecutionOperation()
            prepareResult("Подготовка ответа")
        }.build(),
    )
}