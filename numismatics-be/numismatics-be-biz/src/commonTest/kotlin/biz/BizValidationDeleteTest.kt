package ru.numismatics.backend.biz.test.biz

import validations.lot.runBizTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.stub.StubValues
import kotlin.test.*

class BizValidationDeleteTest : BizValidationBase(Command.DELETE) {

    @Test
    fun `DELETE all good`() = runBizTest {
        val expectedLot = StubValues.lots.first()
        execTest(Lot(id = expectedLot.id, lock = expectedLot.lock)) {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            val actualLot = entityResponse.firstOrNull()
            assertIs<Lot>(actualLot)
            assertEquals(expectedLot, actualLot)
        }
    }

    @Test
    fun `DELETE empty id`() = runBizTest {

        execTest(
            StubValues.lots.map { it.copy(id = LotId.EMPTY) }.first()
        ) {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-id-empty" }
            assertNotNull(er)
        }
    }
}