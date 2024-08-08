package ru.numismatics.backend.biz.repo.lot

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.toUserId
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotPrepareCreateJob() = job {
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == State.RUNNING }
    handle {
        entityRepoPrepare = entityValidated.copy(ownerId = getUserId()).apply {
            setPermissions(entityValidated.getPermissions())
        }
    }
}

// TODO будет реализовано в занятии по управлению пользователями
fun getUserId() = "test-user".toUserId()