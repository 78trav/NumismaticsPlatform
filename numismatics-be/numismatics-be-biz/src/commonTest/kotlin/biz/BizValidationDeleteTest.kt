package ru.numismatics.backend.biz.test.biz

import ru.numismatics.backend.biz.test.validation.runBizTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Country
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.Material
import ru.numismatics.backend.common.models.entities.Section
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.stub.StubProcessor
import kotlin.test.*

class BizValidationDeleteTest : BizValidationBase(Command.DELETE) {

    @Test
    fun `DELETE all good`() = runBizTest {

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
            if (entityType == EntityType.MARKET_PRICE)
                assertTrue(entityValidated.lock.isEmpty())
            else
                assertTrue(entityValidated.lock.isNotEmpty())
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
    fun `DELETE empty id`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.map { it.copy(id = CountryId.EMPTY) }.first(),
                EntityType.MATERIAL to StubProcessor.materials.map { it.copy(id = MaterialId.EMPTY) }.first(),
                EntityType.SECTION to StubProcessor.sections.map { it.copy(id = SectionId.EMPTY) }.first(),
                EntityType.LOT to StubProcessor.lots.map { it.copy(id = LotId.EMPTY) }.first(),
                EntityType.MARKET_PRICE to StubProcessor.lots.map { it.copy(id = LotId.EMPTY) }.first()
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
    fun `DELETE market price without date`() = runBizTest {

        execTest(
            mapOf(
                EntityType.MARKET_PRICE to StubProcessor.lots.map { it.copy(marketPrice = mutableListOf()) }.first()
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-marketprice-empty" }
            assertNotNull(er)
        }

    }
}
