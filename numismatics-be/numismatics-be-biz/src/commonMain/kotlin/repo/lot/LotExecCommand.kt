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

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotExecCommandJob(description: String) = job {
    this.description = description
    on { state == State.RUNNING && repo != null }
    handle {
        val request = DbRequest(
            command = command,
            entity = entityRepoPrepare
        )
        when (val result = repo!!.exec(request)) {
            is DbEntityResponseSuccess<*> -> {
                if (command == Command.SEARCH)
                    result.data.forEach {
                        entityResponse.add(it as Lot)
                    }
                else
                    entityRepoDone.add(result.data.first() as Lot)
            }

            is DbEntityResponseError<*> -> fail(result.errors)
        }
    }
}