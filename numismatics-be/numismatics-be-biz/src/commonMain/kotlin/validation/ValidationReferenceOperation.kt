package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.context.wrongCommand
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.Country
import ru.numismatics.backend.common.models.entities.Material
import ru.numismatics.backend.common.models.entities.Section
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationReferenceOperation() = operation {

    name = "Проверка элемента справочников"

//    on { state == State.RUNNING && entityType in setOf(EntityType.COUNTRY, EntityType.MATERIAL, EntityType.SECTION) }
    on { entityType in setOf(EntityType.COUNTRY, EntityType.MATERIAL, EntityType.SECTION) }

    operation {
        name = "Проверки команды создания элемента справочника"

        on { command == Command.CREATE }

        job {
            name = "Очистка полей, не требуемых в команде создания элемента справочника"

            on { true }

            handle {
                entityValidating = when (entityType) {
                    EntityType.COUNTRY -> (entityValidating as Country).copy(id = CountryId.EMPTY, lock = LockId.NONE)
                    EntityType.MATERIAL -> (entityValidating as Material).copy(
                        id = MaterialId.EMPTY,
                        lock = LockId.NONE
                    )

                    else -> (entityValidating as Section).copy(id = SectionId.EMPTY, lock = LockId.NONE)
                }
            }

        }

        validationNameNotEmpty("Провека заполнения наименования")
        validationNameHasContent("Провека содержания наименования")

        validationDescriptionNotEmpty("Провека заполнения описания")
        validationDescriptionHasContent("Провека содержания описания")
    }

    operation {
        name = "Проверки команды изменения элемента справочника"

        on { command == Command.UPDATE }

        validationIdNotEmpty("Проверка заполнения идентификатора")

        validationLockNotEmpty("Проверка заполнения блокировки")
        validationLockProperFormat("Проверка формата блокировки")

        validationNameNotEmpty("Провека заполнения наименования")
        validationNameHasContent("Провека содержания наименования")

        validationDescriptionNotEmpty("Провека заполнения описания")
        validationDescriptionHasContent("Провека содержания описания")
    }

    operation {
        name = "Проверки команды удаления элемента справочника"

        on { command == Command.DELETE }

        validationIdNotEmpty("Проверка заполнения идентификатора")

        validationLockNotEmpty("Проверка заполнения блокировки")
        validationLockProperFormat("Проверка формата блокировки")

    }

    job {
        name = "Проверка команды"

        on { command !in setOf(Command.CREATE, Command.READ, Command.UPDATE, Command.DELETE) }

        handle {
            fail(wrongCommand())
        }
    }

}
