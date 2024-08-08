package biz.repo

import kotlinx.datetime.Clock
import ru.numismatics.backend.biz.BizProcessor
import ru.numismatics.backend.biz.test.biz.repo.notFoundErrorTest
import ru.numismatics.backend.common.context.CorSettings
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.repo.MockRepo
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.stub.StubValues
import validations.lot.runBizTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BizRepoDeleteTest {

    private val command = Command.DELETE
    private val expectedLot = StubValues.lots.first()

    @Test
    fun `biz delete lot repo test success`() = runBizTest {
        // given
        val processor = BizProcessor(
            CorSettings(
                repo = mapOf(
                    RequestType.TEST to MockRepo { DbEntityResponseSuccess(expectedLot) }
                )
            )
        )

        val context = NumismaticsPlatformContext(
            command = command,
            state = State.NONE,
            timeStart = Clock.System.now(),
            requestType = RequestType.TEST,
            entityRequest = Lot(id = expectedLot.id, lock = expectedLot.lock)
        )

        // when
        processor.exec(context)

        // then
        assertEquals(State.FINISHING, context.state)

        val actualLot = context.entityResponse.firstOrNull()
        assertIs<Lot>(actualLot)

        assertEquals(expectedLot, actualLot)
    }

    @Test
    fun `biz delete lot repo test not found`() = notFoundErrorTest(Lot(id = expectedLot.id, lock = expectedLot.lock), command)

}