package ru.numismatics.backend.biz.test.biz

import validations.lot.runBizTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.isEmpty
import ru.numismatics.backend.stub.StubValues
import kotlin.test.*

class BizValidationSearchTest : BizValidationBase(Command.SEARCH) {

    @Test
    fun `SEARCH lot all good`() = runBizTest {
        execTest(
            StubValues.lots.first()
        ) {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name, entityValidated.name)
            assertEquals(entityRequest.description, entityValidated.description)
            assertTrue(entityValidated.lock.isEmpty())
            assertTrue(entityValidated.ownerId.isEmpty())
            assertTrue(entityValidated.id.isEmpty())
        }
    }

    @Test
    fun `SEARCH trim string fields`() = runBizTest {

        val requestLot = StubValues.lots.first()

        execTest(
            requestLot.copy(
                name = forTrim(requestLot.name),
                description = forTrim(requestLot.description),
                catalogueNumber = forTrim(requestLot.catalogueNumber),
                denomination = forTrim(requestLot.denomination),
                serialNumber = forTrim(requestLot.serialNumber)
            )
        ) {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(requestLot.name, entityValidated.name)
            assertEquals(requestLot.description, entityValidated.description)
            assertTrue(entityValidated.lock.isEmpty())

            assertEquals(requestLot.catalogueNumber, entityValidated.catalogueNumber)
            assertEquals(requestLot.denomination, entityValidated.denomination)
            assertEquals(requestLot.serialNumber, entityValidated.serialNumber)
            assertTrue(entityValidated.ownerId.isEmpty())
            assertTrue(entityValidated.id.isEmpty())
        }
    }

    @Test
    fun `SEARCH no conditions`() = runBizTest {
        execTest(Lot.EMPTY) {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-search-empty" }
            assertNotNull(er)
        }
    }
}