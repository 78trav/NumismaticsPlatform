package ru.numismatics.backend.biz.validation

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.errorValidation
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext>.validationYear() = job {
    name = "Проверка года выпуска"

    on {
        entityType == EntityType.LOT
                && entityValidating is Lot
                && (entityValidating as Lot).year > timeStart.toLocalDateTime(TimeZone.currentSystemDefault()).year.toUInt()
    }

    handle {
        fail(
            errorValidation(
                field = "year",
                violationCode = "badValue",
                description = "Год должен быть не больше текущего"
            )
        )
    }
}
