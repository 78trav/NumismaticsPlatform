package ru.numismatics.backend.biz

import ru.numismatics.backend.biz.stubs.*
import ru.numismatics.backend.biz.validation.validationOperation
import ru.numismatics.backend.common.context.CorSettings
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.Processor
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.process

class BizProcessor(corSettings: CorSettings) : Processor<NumismaticsPlatformContext>(corSettings) {

    override suspend fun exec(context: NumismaticsPlatformContext) {
        processes[context.requestType]?.build()?.exec(context.also { it.corSettings = corSettings })
    }


    private val processes = mapOf<RequestType, CorOperationDSL<NumismaticsPlatformContext>>(

        RequestType.STUB to process {
            start()
            stubReferenceOperation()
            stubLotOperation()
            stubMarketPriceJob()
            stubOtherOperation()
        },

        RequestType.TEST to process {
            start()
            validationOperation()
        },

        RequestType.PROD to process {
            start()
            validationOperation()

        }
    )

}
