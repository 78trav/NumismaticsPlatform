package ru.numismatics.backend.biz.test.validation

import ru.numismatics.backend.biz.validation.validationNameHasContent
import ru.numismatics.backend.biz.validation.validationStart
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.entities.Country
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.Material
import ru.numismatics.backend.common.models.entities.Section
import ru.numismatics.backend.common.models.id.CountryId
import ru.numismatics.backend.common.models.id.LotId
import ru.numismatics.backend.common.models.id.MaterialId
import ru.numismatics.backend.common.models.id.SectionId
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ValidationNameHasContentTest {

    companion object {
        private val process = process<NumismaticsPlatformContext> {
            validationStart()
            validationNameHasContent("")
        }.build()

        private const val ERROR_CODE = "validation-name-noContent"
    }

    @Test
    fun `empty name`() = runBizTest {

        execTest(
            ERROR_CODE,
            mapOf(
                EntityType.COUNTRY to Country.EMPTY,
                EntityType.MATERIAL to Material.EMPTY,
                EntityType.SECTION to Section.EMPTY,
                EntityType.LOT to Lot.EMPTY,
                EntityType.MARKET_PRICE to Lot.EMPTY
            ),
            process
        )
        { error ->
            assertNull(error)
        }
    }

    @Test
    fun `bad name`() = runBizTest {

        val name = "12!@#$%^&*()_+-="
        execTest(
            ERROR_CODE,
            mapOf(
                EntityType.COUNTRY to Country(CountryId(1u), name = name),
                EntityType.MATERIAL to Material(MaterialId(2u), name = name),
                EntityType.SECTION to Section(SectionId(3u), name = name),
                EntityType.LOT to Lot(LotId(4u), name = name),
                EntityType.MARKET_PRICE to Lot(LotId(4u), name = name)
            ),
            process
        )
        { error ->
            assertNotNull(error)
        }
    }

    @Test
    fun `good name`() = runBizTest {

        execTest(
            ERROR_CODE,
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.first(),
                EntityType.MATERIAL to StubProcessor.materials.first(),
                EntityType.SECTION to StubProcessor.sections.first(),
                EntityType.LOT to StubProcessor.lots.first(),
                EntityType.MARKET_PRICE to StubProcessor.lots.first()
            ),
            process
        )
        { error ->
            assertNull(error)
        }
    }
}
