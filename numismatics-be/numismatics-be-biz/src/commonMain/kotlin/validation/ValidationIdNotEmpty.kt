package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.helpers.errorValidation
import ru.numismatics.backend.common.models.entities.*
import ru.numismatics.backend.common.models.id.EMPTY_ID
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.validationIdNotEmpty(name: String) = job {
    this.name = name
    on { entityValidating.id() == EMPTY_ID }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}