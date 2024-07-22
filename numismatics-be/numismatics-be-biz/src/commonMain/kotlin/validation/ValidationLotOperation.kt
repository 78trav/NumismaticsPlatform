package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.context.wrongCommand
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationLotOperation() = operation {

    name = "Проверка элемента коллекции"

//    on { state == State.RUNNING && entityType == EntityType.LOT }
    on { entityType == EntityType.LOT }

    job {
        name = "Очистка строковых полей"

        on { true }

        handle {
            entityValidating = (entityValidating as Lot).run {
                copy(
                    ownerId = ownerId.asString().trim().toUserId(),
                    catalogueNumber = catalogueNumber.trim(),
                    denomination = denomination.trim(),
                    serialNumber = serialNumber.trim(),
                )
            }
        }
    }

    job {
        name = "Очистка полей, не требуемых в команде создания и поиска лота"

        on { command in setOf(Command.CREATE, Command.SEARCH) }

        handle {
            entityValidating = (entityValidating as Lot).run {
                copy(
                    id = LotId.EMPTY,
                    lock = LockId.NONE,
                    ownerId = UserId.EMPTY
                )
            }
        }
    }


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
                (entityValidating as Lot).run {
                    name.isEmpty()
                            && description.isEmpty()
                            && year == 0u
                            && countryId.isEmpty()
                            && materialId.isEmpty()
                            && denomination.isEmpty()
                            && condition == Condition.UNDEFINED
                }

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

}
