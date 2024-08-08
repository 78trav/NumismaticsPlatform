package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.helpers.errorValidation
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.validationDescriptionNotEmpty(name: String) =
    job {
        this.name = name
        on { entityValidating.description.isEmpty() }
        handle {
            fail(
                errorValidation(
                    field = "description",
                    violationCode = "empty",
                    description = "field must not be empty"
                )
            )
        }
    }