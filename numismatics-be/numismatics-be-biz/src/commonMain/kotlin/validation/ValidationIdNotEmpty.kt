package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.models.core.errorValidation
import ru.numismatics.backend.common.models.entities.*
import ru.numismatics.backend.common.models.id.isEmpty
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationIdNotEmpty(name: String) = job {
    this.name = name

    on {
        when (entityValidating) {
            is Country -> (entityValidating as Country).id.isEmpty()
            is Material -> (entityValidating as Material).id.isEmpty()
            is Section -> (entityValidating as Section).id.isEmpty()
            is Lot -> (entityValidating as Lot).id.isEmpty()
            EmptyEntity -> true
        }
    }

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
