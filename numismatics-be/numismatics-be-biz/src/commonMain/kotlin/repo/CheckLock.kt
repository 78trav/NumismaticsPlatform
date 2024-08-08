package ru.numismatics.backend.biz.repo

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.repo.errorRepoConcurrency
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.checkLockJob() = job {
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == State.RUNNING && entityValidated.lock != entityRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(entityRepoRead, entityValidated.lock))
    }
}