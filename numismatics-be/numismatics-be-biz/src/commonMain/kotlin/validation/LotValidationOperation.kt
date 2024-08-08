package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.context.wrongCommand
import ru.numismatics.backend.common.helpers.errorValidation
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotDeepCopyJob() = job {
    name = "Копирование объекта в entityValidating"
    on { true }
    handle {
        entityValidating = entityRequest.deepCopy()
    }
}

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotClearCommonFieldsJob() = job {
    name = "Очистка строковых полей"
    on { true }
    handle {
        entityValidating = entityValidating.copy(
            name = entityValidating.name.trim(),
            description = entityValidating.description.trim(),
            ownerId = entityValidating.ownerId.asString().trim().toUserId(),
            catalogueNumber = entityValidating.catalogueNumber.trim(),
            denomination = entityValidating.denomination.trim(),
            serialNumber = entityValidating.serialNumber.trim(),
            lock = entityValidating.lock.asString().trim().toLockId()
        )
    }
}

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotClearFieldsForCreateSearchJob() = job {
    name = "Очистка полей, не требуемых в команде создания и поиска лота"
    on { command in setOf(Command.CREATE, Command.SEARCH) }
    handle {
        entityValidating = entityValidating.copy(
            id = LotId.EMPTY,
            lock = LockId.NONE,
            ownerId = UserId.EMPTY
        )
    }
}

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotValidationOperation() = operation {
    name = "Проверки для элемента коллекции"
    on { state == State.RUNNING }

//    job {
//        on { true }
//        handle {
//            println(this)
//        }
//    }

    lotDeepCopyJob()
    lotClearCommonFieldsJob()
    lotClearFieldsForCreateSearchJob()

//    job {
//        on { true }
//        handle {
//            println(this)
//        }
//    }

    operation {
        name = "Проверки команды создания лота"
        on { command == Command.CREATE }
        validationNameNotEmpty("Провека заполнения наименования")
        validationNameHasContent("Провека содержания наименования")
        validationDescriptionNotEmpty("Провека заполнения описания")
        validationDescriptionHasContent("Провека содержания описания")
        validationYear()
    }

    operation {
        name = "Проверки команды чтения лота"
        on { command == Command.READ }
        validationIdNotEmpty("Проверка заполнения идентификатора")
    }

    operation {
        name = "Проверки команды изменения лота"
        on { command == Command.UPDATE }
        validationIdNotEmpty("Проверка заполнения идентификатора")
        validationLockNotEmpty("Проверка заполнения блокировки")
        validationLockProperFormat("Проверка формата блокировки")
        validationNameNotEmpty("Провека заполнения наименования")
        validationNameHasContent("Провека содержания наименования")
        validationDescriptionNotEmpty("Провека заполнения описания")
        validationDescriptionHasContent("Провека содержания описания")
        validationYear()
    }

    operation {
        name = "Проверки команды удаления лота"
        on { command == Command.DELETE }
        validationIdNotEmpty("Проверка заполнения идентификатора")
        validationLockNotEmpty("Проверка заполнения блокировки")
        validationLockProperFormat("Проверка формата блокировки")
    }

    operation {
        name = "Проверки команды поиска лотов"
        on { command == Command.SEARCH }
        job {
            name = "Проверка наличия хотя бы одного условия поиска"
            on {
                entityValidating.description.isEmpty()
                        && entityValidating.year == 0u
                        && entityValidating.countryId.isEmpty()
                        && entityValidating.materialId.isEmpty()
                        && entityValidating.sectionId.isEmpty()
                        && entityValidating.condition == Condition.UNDEFINED
            }
            handle {
                fail(
                    errorValidation(
                        field = "search",
                        violationCode = "empty",
                        description = "Must at least one condition"
                    )
                )
            }
        }
        validationYear()
    }

    job {
        name = "Проверка команды"
        on { command !in setOf(Command.CREATE, Command.READ, Command.UPDATE, Command.DELETE, Command.SEARCH) }
        handle {
            fail(wrongCommand())
        }
    }

    validationFinish()
}