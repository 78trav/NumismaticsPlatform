package ru.numismatics.backend.biz.exec

import ru.numismatics.backend.biz.repo.checkLockJob
import ru.numismatics.backend.biz.repo.lot.*
import ru.numismatics.backend.biz.repo.lot.lotPrepareCreateJob
import ru.numismatics.backend.biz.repo.lot.lotPrepareUpdateJob
import ru.numismatics.backend.biz.repo.lot.lotReadJob
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotCommandExecutionOperation() = operation {
    name = "Обработка команд работы с элементом коллекции"
    on { state == State.RUNNING }

    operation {
        name = "Команда CREATE"
        on { state == State.RUNNING && command == Command.CREATE }
        lotPrepareCreateJob()
        lotExecCommandJob("Добавление лота в БД")
    }

    operation {
        name = "Команда READ"
        on { state == State.RUNNING && command == Command.READ }
        lotReadJob()
        job {
            name = "Подготовка ответа для READ"
            on { state == State.RUNNING }
            handle { entityRepoDone.add(entityRepoRead) }
        }
    }

    operation {
        name = "Команда UPDATE"
        on { state == State.RUNNING && command == Command.UPDATE }
        lotReadJob()
        checkLockJob()
        lotPrepareUpdateJob()
        lotExecCommandJob("Изменение лота в БД")
    }

    operation {
        name = "Команда DELETE"
        on { state == State.RUNNING && command == Command.DELETE }
        lotReadJob()
        checkLockJob()
        job {
            description = "Подготовка данных к удалению из БД"
            on { state == State.RUNNING }
            handle {
                entityRepoPrepare = entityValidated.deepCopy()
            }
        }
        lotExecCommandJob("Удаление лота в БД")
    }

    operation {
        name = "Команда SEARCH"
        on { state == State.RUNNING && command == Command.SEARCH }
        lotExecCommandJob("Поиск лотов в БД")
    }

}