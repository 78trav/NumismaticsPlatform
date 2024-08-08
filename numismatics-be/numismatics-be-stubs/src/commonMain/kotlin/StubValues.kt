package ru.numismatics.backend.stub

import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.*
import ru.numismatics.backend.common.models.id.*

abstract class StubValues {

    companion object {

        private val lock = "test-lock".toLockId()

        val countries = listOf(
            Country(CountryId(1U), "СССР", "Союз Советских Социалистических Республик", lock).apply {
                setPermissions(setOf(EntityPermission.READ))
            },
            Country(CountryId(2U), "Россия", "Российская Федерация").apply {
                setPermissions(setOf(EntityPermission.READ))
            }
        )

        val materials = listOf(
            Material(
                id = MaterialId(1U),
                name = "Серебро 925",
                description = "Серебро 925 пробы",
                probe = 925f,
                lock = lock
            ).apply {
                setPermissions(setOf(EntityPermission.READ))
            },
            Material(id = MaterialId(2U), name = "Золото 999", description = "Золото 999 пробы", probe = 999f).apply {
                setPermissions(setOf(EntityPermission.READ))
            }
        )

        val sections = listOf(
            Section(
                SectionId(1U),
                "Мультики",
                "Российская (советская) мультипликация",
                parentId = SectionId(10U),
                lock = lock
            ).apply {
                setPermissions(setOf(EntityPermission.READ))
            },
            Section(SectionId(2U), "Сказки", "Легенды и сказки народов России").apply {
                setPermissions(setOf(EntityPermission.READ))
            },
            Section(SectionId(3U), "Города").apply {
                setPermissions(setOf(EntityPermission.READ))
            }
        )

        //        val entitiesCommands = mapOf(
//            EntityType.COUNTRY to setOf(Command.CREATE, Command.READ, Command.UPDATE, Command.DELETE),
//            EntityType.MATERIAL to setOf(Command.CREATE, Command.READ, Command.UPDATE, Command.DELETE),
//            EntityType.SECTION to setOf(Command.CREATE, Command.READ, Command.UPDATE, Command.DELETE),
//            EntityType.LOT to setOf(Command.CREATE, Command.READ, Command.UPDATE, Command.DELETE, Command.SEARCH),
//            EntityType.MARKET_PRICE to setOf(Command.CREATE, Command.READ, Command.DELETE)
//        )
        val entitiesCommands = mapOf(
            Lot::class to setOf(Command.CREATE, Command.READ, Command.UPDATE, Command.DELETE, Command.SEARCH)
        )

        val lots = listOf(
            Lot(
                id = LotId(1U),
                name = "Киров 650",
                description = "650-летие основания г. Кирова",
                isCoin = true,
                year = 2024u,
                catalogueNumber = "5111-0502",
                denomination = "3 рубля",
                weight = 31.1f,
                condition = Condition.PF,
                quantity = 1u,
                countryId = CountryId(2u),
                materialId = MaterialId(1u),
                sectionId = SectionId(3u),
                ownerId = "34da1510-a17b-11e9-728d-00241d9157c0".toUserId(),
                lock = lock
            ).apply {
                setPermissions(setOf(EntityPermission.READ))
            },
            Lot(
                id = LotId(2U),
                name = "Ну погоди",
                description = "Ну, погоди!",
                isCoin = true,
                year = 2018u,
                catalogueNumber = "5111-0387",
                denomination = "3 рубля",
                weight = 31.1f,
                condition = Condition.XF_PLUS,
                quantity = 1u,
                countryId = CountryId(2u),
                materialId = MaterialId(1u),
                sectionId = SectionId(1u)
            ).apply {
                setPermissions(setOf(EntityPermission.READ))
            }
        )

        val error = Error(
            code = "err",
            group = "test",
            field = "test",
            message = "some testing error"
        )
    }

}