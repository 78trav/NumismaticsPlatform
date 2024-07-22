package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.context.wrongCommand
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.core.errorValidation
import ru.numismatics.backend.common.models.entities.Country
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.Material
import ru.numismatics.backend.common.models.entities.Section
import ru.numismatics.backend.common.models.id.CountryId
import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.MaterialId
import ru.numismatics.backend.common.models.id.SectionId
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationMarketPriceOperation() = operation {

    name = "Проверка рыночной стоимости"

//    on { state == State.RUNNING && entityType == EntityType.MARKET_PRICE }
    on { entityType == EntityType.MARKET_PRICE }

    job {
        name = "Очистка полей, не требуемых в командах работы с рыночной стоимостью"

        on { true }

        handle {
            entityValidating = (entityValidating as Lot).copy(lock = LockId.NONE)
        }
    }


    operation {
        name = "Проверки команды создания и удаления рыночной стоимости"

        on { command in setOf(Command.CREATE, Command.DELETE) }

        validationIdNotEmpty("Проверка заполнения идентификатора")

        job {
            name = "Проверка заполнения рыночной стоимости"

            on { (entityValidating as Lot).marketPrice.size == 0 }

            handle {
                fail(
                    errorValidation(
                        field = "marketprice",
                        violationCode = "empty",
                        description = "field must not be empty"
                    )
                )
            }
        }
    }

    operation {
        name = "Проверки команды чтения рыночной стоимости"

        on { command == Command.READ }

        validationIdNotEmpty("Проверка заполнения идентификатора")
    }

    job {
        name = "Проверка команды"

        on { command !in setOf(Command.CREATE, Command.READ, Command.DELETE) }

        handle {
            fail(wrongCommand())
        }
    }

}
