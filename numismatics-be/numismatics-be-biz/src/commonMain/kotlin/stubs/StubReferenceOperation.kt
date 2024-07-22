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

internal fun CorOperationDSL<NumismaticsPlatformContext>.stubReferenceOperation() = operation {
    name = "Работа со справочниками"
    on {
        state == State.RUNNING && requestType == RequestType.STUB && stubCase == Stubs.SUCCESS && entityType in setOf(
            EntityType.COUNTRY,
            EntityType.MATERIAL,
            EntityType.SECTION
        ) && StubProcessor.entitiesCommands[entityType]?.contains(command) ?: false
    }

    stubCUDReferenceOperation()
    stubReadReferenceOperation()

}

private fun CorOperationDSL<NumismaticsPlatformContext>.stubCUDReferenceOperation() = operation {

    on {
        state == State.RUNNING && setOf(Command.CREATE, Command.UPDATE, Command.DELETE).contains(command)
    }

    val t = "Создание, изменение, удаление"
    name = "$t элемента справочника"
    description = "Имитация успешной обработки"

    job {
        name = "$t страны"
        on { entityType == EntityType.COUNTRY }
        handle {
            state = State.FINISHING
            entityResponse.add(StubProcessor.countries.first())
        }
    }

    job {
        name = "$t материала"
        on { entityType == EntityType.MATERIAL }
        handle {
            state = State.FINISHING
            entityResponse.add(StubProcessor.materials.first())
        }
    }

    job {
        name = "$t раздела"
        on { entityType == EntityType.SECTION }
        handle {
            state = State.FINISHING
            entityResponse.add(StubProcessor.sections.first())
        }
    }

}

private fun CorOperationDSL<NumismaticsPlatformContext>.stubReadReferenceOperation() = operation {

    on {
        state == State.RUNNING && command == Command.READ
    }

    val t = "Чтение"
    name = "$t элемента справочника"
    description = "Имитация успешной обработки"

    job {
        name = "$t страны"
        on { entityType == EntityType.COUNTRY }
        handle {
            state = State.FINISHING
            if (entityRequest.isEmpty()) entityResponse.addAll(StubProcessor.countries)
            else entityResponse.add(StubProcessor.countries.first())
        }
    }

    job {
        name = "$t материала"
        on { entityType == EntityType.MATERIAL }
        handle {
            state = State.FINISHING
            if (entityRequest.isEmpty()) entityResponse.addAll(StubProcessor.materials)
            else entityResponse.add(StubProcessor.materials.first())
        }
    }

    job {
        name = "$t раздела"
        on { entityType == EntityType.SECTION }
        handle {
            state = State.FINISHING
            if (entityRequest.isEmpty()) entityResponse.addAll(StubProcessor.sections)
            else entityResponse.add(StubProcessor.sections.first())
        }
    }

}
