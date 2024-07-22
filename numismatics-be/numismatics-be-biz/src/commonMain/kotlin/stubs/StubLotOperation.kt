package ru.numismatics.backend.biz.stubs

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext>.stubLotOperation() = operation {
    name = "Работа с элементом коллекции"
    on { state == State.RUNNING && requestType == RequestType.STUB && stubCase == Stubs.SUCCESS && entityType == EntityType.LOT && StubProcessor.entitiesCommands[entityType]?.contains(command) ?: false}

    job {
        name = "Создание, изменение, удаление, поиск с элемента коллекции"
        description = "Имитация успешной обработки"

        on {
            state == State.RUNNING && setOf(Command.CREATE, Command.UPDATE, Command.DELETE, Command.SEARCH).contains(command)
        }

        handle {
            state = State.FINISHING
            entityResponse.add(StubProcessor.lots.first())
        }

    }

    job {
        name = "Чтение элемента коллекции"
        description = "Имитация успешной обработки"

        on { state == State.RUNNING && command == Command.READ }

        handle {
            state = State.FINISHING
            if (entityRequest.isEmpty()) entityResponse.addAll(StubProcessor.lots)
            else entityResponse.add(StubProcessor.lots.first())
        }

    }

//    stubFailJob("Прочие команды для работы с элементом коллекции")

}
