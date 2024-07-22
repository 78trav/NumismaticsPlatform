package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.models.core.errorValidation
import ru.numismatics.backend.common.models.id.isNotEmpty
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationLockProperFormat(name: String) = job {
    this.name = name

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { entityValidating.lock.isNotEmpty() && !entityValidating.lock.asString().matches(regExp) }

    handle {
        val lock = entityValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $lock must contain only"
            )
        )
    }

}
