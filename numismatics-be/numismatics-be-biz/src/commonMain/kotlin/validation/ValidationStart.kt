package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.id.toLockId
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationStart() = job {
    name = "Копирование объекта в entityValidating с очисткой общих строковых полей"

    on { true }

    handle {
        entityValidating = entityRequest.deepCopy(
            name = entityRequest.name.trim(),
            description = entityRequest.description.trim(),
            lock = entityRequest.lock.asString().trim().toLockId()
        )
    }
}
