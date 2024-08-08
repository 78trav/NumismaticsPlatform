package ru.numismatics.backend.biz.repo

import ru.numismatics.backend.biz.exceptions.DbNotConfiguredException
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.helpers.errorSystem
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.initRepo(name: String) = job {
    this.name = name
    description = "Определение рабочего репозитория в зависимости от запрошенного режима работы"
    on { true }
    handle {
        repo = corSettings.repo[requestType]
        if (requestType != RequestType.STUB && repo == null)
            fail(
                errorSystem(
                    violationCode = "dbNotConfigured",
                    exception = DbNotConfiguredException(requestType)
                )
            )
    }
}