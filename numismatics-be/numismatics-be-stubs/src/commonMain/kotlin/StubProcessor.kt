package ru.numismatics.backend.stub

import kotlinx.datetime.LocalDate
import ru.numismatics.backend.common.IProcessor
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.*
import ru.numismatics.backend.common.models.id.*

class StubProcessor : IProcessor {

    override suspend fun exec(context: NumismaticsPlatformContext) {

        context.state = State.RUNNING
        if (context.command !in setOf(Command.WS_INIT, Command.WS_CLOSE))
            when (context.entityType) {

                EntityType.COUNTRY -> stubCommand(context, countries)
                EntityType.MATERIAL -> stubCommand(context, materials)
                EntityType.SECTION -> stubCommand(context, sections)
                EntityType.MARKET_PRICE -> stubCommand(context, lots, setOf(Command.CREATE, Command.DELETE))
                EntityType.LOT -> stubCommand(
                    context,
                    lots,
                    setOf(Command.CREATE, Command.UPDATE, Command.DELETE, Command.SEARCH)
                )

                else -> {
                    context.state = State.FAILING
                    context.errors.add(Error(message = "Неизвестая сущность"))
                }
            }

    }

    private fun stubCommand(
        context: NumismaticsPlatformContext,
        source: List<Entity>,
        commands: Set<Command> = setOf(Command.CREATE, Command.UPDATE, Command.DELETE)
    ) {

        when (context.command) {

            in commands -> context.entityResponse.add(source.first())
            Command.READ -> if (context.entityRequest.isEmpty()) context.entityResponse.addAll(source) else context.entityResponse.add(
                source.first()
            )

            else -> {
                context.state = State.FAILING
                context.errors.add(Error(message = "Операция ${context.command} для сущности '${context.entityType.description()}' не поддерживается"))
            }
        }
    }


    companion object {

        val countries = listOf(
            Country(CountryId(1U), "СССР", "Союз Советских Социалистических Республик").apply {
                permissions.add(EntityPermission.READ)
            },
            Country(CountryId(2U), "Россия", "Российская Федерация").apply {
                permissions.add(EntityPermission.READ)
            }
        )

        val materials = listOf(
            Material(id = MaterialId(1U), name = "Серебро 925", description = "Серебро 925 пробы", probe = 925f).apply {
                permissions.add(EntityPermission.READ)
            },
            Material(id = MaterialId(2U), name = "Золото 999", description = "Золото 999 пробы", probe = 999f).apply {
                permissions.add(EntityPermission.READ)
            }
        )

        val sections = listOf(
            Section(
                SectionId(1U),
                "Мультики",
                "Российская (советская) мультипликация",
                parentId = SectionId(10U)
            ).apply {
                permissions.add(EntityPermission.READ)
            },
            Section(SectionId(2U), "Сказки", "Легенды и сказки народов России").apply {
                permissions.add(EntityPermission.READ)
            },
            Section(SectionId(3U), "Города").apply {
                permissions.add(EntityPermission.READ)
            }
        )

        const val PHOTO_1 = "фото1"
        const val PHOTO_2 = "фото2"

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
                marketPrice = mutableListOf(
                    MarketPrice(LocalDate(2024, 7, 7), 10000f),
                    MarketPrice(LocalDate(2024, 6, 7), 8900f)
                ),
                sectionId = SectionId(3u),
                photos = mutableListOf(Base64String(PHOTO_1), Base64String(PHOTO_2)),
                lock = "test-lock".toLockId()
            ).apply {
                permissions.add(EntityPermission.READ)
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
                condition = Condition.PF,
                quantity = 1u,
                countryId = CountryId(2u),
                materialId = MaterialId(1u),
                marketPrice = mutableListOf(
                    MarketPrice(LocalDate(2018, 8, 1), 5000f),
                    MarketPrice(LocalDate(2024, 6, 7), 150_000f)
                ),
                sectionId = SectionId(1u)
            ).apply {
                permissions.add(EntityPermission.READ)
            }
        )

    }

}
