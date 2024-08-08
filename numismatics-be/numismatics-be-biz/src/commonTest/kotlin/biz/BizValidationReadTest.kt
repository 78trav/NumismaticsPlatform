package ru.numismatics.backend.biz.test.biz

import validations.lot.runBizTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.stub.StubValues
import kotlin.test.*

class BizValidationReadTest : BizValidationBase(Command.READ) {

    @Test
    fun `READ all good`() = runBizTest {
        val expectedLot = StubValues.lots.first()
        execTest(Lot(id = expectedLot.id)) {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            val actualLot = entityResponse.firstOrNull()
            assertIs<Lot>(actualLot)
            assertEquals(expectedLot, actualLot)
//            println(this)
        }
    }

    @Test
    fun `READ lot empty id`() = runBizTest {
        execTest(Lot.EMPTY) {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-id-empty" }
            assertNotNull(er)
        }
    }
}