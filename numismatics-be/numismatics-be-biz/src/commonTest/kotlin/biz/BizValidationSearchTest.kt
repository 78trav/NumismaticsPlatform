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
import ru.numismatics.backend.stub.StubProcessor
import kotlin.test.*

class BizValidationSearchTest : BizValidationBase(Command.SEARCH) {

    @Test
    fun `SEARCH lot all good`() = runBizTest {

        execTest(
            mapOf(
                EntityType.LOT to StubProcessor.lots.first()
            )
        )
        {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name, entityValidated.name)
            assertEquals(entityRequest.description, entityValidated.description)
            assertTrue(entityValidated.lock.isEmpty())
            (entityValidated as Lot).run {
                assertTrue(ownerId.isEmpty())
                assertTrue(id.isEmpty())
            }
        }
    }

    @Test
    fun `SEARCH trim string fields`() = runBizTest {

        val requestLot = StubProcessor.lots.first()

        execTest(
            mapOf(
                EntityType.LOT to requestLot.copy(
                    name = forTrim(requestLot.name),
                    description = forTrim(requestLot.description),
                    catalogueNumber = forTrim(requestLot.catalogueNumber),
                    denomination = forTrim(requestLot.denomination),
                    serialNumber = forTrim(requestLot.serialNumber)
                )
            )
        )
        {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(requestLot.name, entityValidated.name)
            assertEquals(requestLot.description, entityValidated.description)
            assertTrue(entityValidated.lock.isEmpty())

            (entityValidated as Lot).run {
                assertEquals(requestLot.catalogueNumber, this.catalogueNumber)
                assertEquals(requestLot.denomination, this.denomination)
                assertEquals(requestLot.serialNumber, this.serialNumber)
                assertTrue(ownerId.isEmpty())
                assertTrue(id.isEmpty())
            }
        }
    }

    @Test
    fun `SEARCH no conditions`() = runBizTest {

        execTest(
            mapOf(
                EntityType.LOT to Lot.EMPTY
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-search-empty" }
            assertNotNull(er)
        }
    }


    @Test
    fun `SEARCH reference market price`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to Country.EMPTY,
                EntityType.MATERIAL to Material.EMPTY,
                EntityType.SECTION to Section.EMPTY,
                EntityType.MARKET_PRICE to Lot.EMPTY
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-command-badValue" }
            assertNotNull(er)
        }

    }

}
