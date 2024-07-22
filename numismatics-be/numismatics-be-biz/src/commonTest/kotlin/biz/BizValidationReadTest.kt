package ru.numismatics.backend.biz.test.biz

import ru.numismatics.backend.biz.test.validation.runBizTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Country
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.Material
import ru.numismatics.backend.common.models.entities.Section
import ru.numismatics.backend.common.models.id.isEmpty
import ru.numismatics.backend.common.models.id.isNotEmpty
import ru.numismatics.backend.stub.StubProcessor
import kotlin.test.*

class BizValidationReadTest : BizValidationBase(Command.READ) {

    @Test
    fun `READ all good`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.first(),
                EntityType.MATERIAL to StubProcessor.materials.first(),
                EntityType.SECTION to StubProcessor.sections.first(),
                EntityType.LOT to StubProcessor.lots.first(),
                EntityType.MARKET_PRICE to StubProcessor.lots.first()
            )
        )
        {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            when (entityType) {
                EntityType.COUNTRY -> assertTrue { (entityValidated as Country).id.isNotEmpty() }
                EntityType.MATERIAL -> assertTrue { (entityValidated as Material).id.isNotEmpty() }
                EntityType.SECTION -> assertTrue { (entityValidated as Section).id.isNotEmpty() }
                EntityType.LOT -> assertTrue { (entityValidated as Lot).id.isNotEmpty() }
                EntityType.MARKET_PRICE -> assertTrue { (entityValidated as Lot).id.isNotEmpty() }
                EntityType.UNDEFINED -> assertTrue(false)
            }

        }

    }

    @Test
    fun `READ lot market price empty id`() = runBizTest {

        execTest(
            mapOf(
                EntityType.LOT to Lot.EMPTY,
                EntityType.MARKET_PRICE to Lot.EMPTY
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-id-empty" }
            assertNotNull(er)
        }

    }

    @Test
    fun `READ reference empty id`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to Country.EMPTY,
                EntityType.MATERIAL to Material.EMPTY,
                EntityType.SECTION to Section.EMPTY
            )
        )
        {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            when (entityType) {
                EntityType.COUNTRY -> assertTrue { (entityValidated as Country).id.isEmpty() }
                EntityType.MATERIAL -> assertTrue { (entityValidated as Material).id.isEmpty() }
                EntityType.SECTION -> assertTrue { (entityValidated as Section).id.isEmpty() }
                else -> assertTrue(false)
            }
        }

    }

}
