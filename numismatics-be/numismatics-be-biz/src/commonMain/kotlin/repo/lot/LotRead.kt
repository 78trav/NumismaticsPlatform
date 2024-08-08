package ru.numismatics.backend.biz.repo.lot

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.repo.base.DbEntityResponseError
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.common.repo.base.DbRequest
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotReadJob() = job {
    description = "Чтение лота из БД"
    on { state == State.RUNNING && repo != null }
    handle {
        val request = DbRequest(
            command = Command.READ,
            entity = entityValidated
        )
        when (val result = repo!!.exec(request)) {
            is DbEntityResponseSuccess<*> -> entityRepoRead = result.data.first() as Lot
            is DbEntityResponseError<*> -> fail(result.errors)
        }
    }
}