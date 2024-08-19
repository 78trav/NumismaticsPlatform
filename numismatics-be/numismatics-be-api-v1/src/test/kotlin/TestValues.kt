package ru.numismatics.backend.api.v1.test

import ru.numismatics.backend.api.v1.models.Debug
import ru.numismatics.backend.api.v1.models.RequestDebugMode
import ru.numismatics.backend.api.v1.models.RequestDebugStubs
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*

abstract class TestValues {

    companion object {

        val debug = Debug(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.SUCCESS
        )

        val error = Error(
            code = "err",
            group = "request",
            field = "name",
            message = "wrong name"
        )

        const val PHOTO_1 = "фото1"
        const val PHOTO_2 = "фото2"

        val lotInt = Lot(
            id = LotId(100UL),
            ownerId = UserId("34da1510-a17b-11e9-728d-00241d9157c0"),
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024U,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = Condition.PF,
            quantity = 1U,
            photos = mutableListOf(Base64String(PHOTO_1), Base64String(PHOTO_2)),
            countryId = CountryId(2U),
            materialId = MaterialId(3U),
            lock = LockId("5698409")
        ).apply {
            permissions.add(EntityPermission.READ)
            permissions.add(EntityPermission.UPDATE)
            permissions.add(EntityPermission.DELETE)
        }

        val filledContext = NumismaticsPlatformContext(
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("832"),
            entityType = EntityType.LOT,
            entityResponse = mutableListOf(lotInt)
        )


    }
}
