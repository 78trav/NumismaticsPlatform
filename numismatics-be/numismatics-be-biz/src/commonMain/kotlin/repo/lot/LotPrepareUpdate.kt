package ru.numismatics.backend.biz.repo.lot

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun CorOperationDSL<NumismaticsPlatformContext<Lot>>.lotPrepareUpdateJob() = job {
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == State.RUNNING }
    handle {
        entityRepoPrepare = entityRepoRead.copy(
            name = entityValidated.name,
            description = entityValidated.description,
            lock = entityValidated.lock,
            sectionId = entityValidated.sectionId,
            isCoin = entityValidated.isCoin,
            year = entityValidated.year,
            countryId = entityValidated.countryId,
            catalogueNumber = entityValidated.catalogueNumber,
            denomination = entityValidated.denomination,
            materialId = entityValidated.materialId,
            weight = entityValidated.weight,
            condition = entityValidated.condition,
            serialNumber = entityValidated.serialNumber,
            quantity = entityValidated.quantity
        )
    }
}