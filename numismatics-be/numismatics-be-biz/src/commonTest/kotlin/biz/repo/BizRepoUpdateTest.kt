package ru.numismatics.backend.biz.test.biz.repo

import kotlinx.datetime.Clock
import ru.numismatics.backend.biz.BizProcessor
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
import kotlin.test.*

class BizRepoUpdateTest {

    private val command = Command.UPDATE

    @Test
    fun `biz update lot repo test success`() = runBizTest {
        // given
        val expectedLot = StubValues.lots.first()
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
            entityRequest = expectedLot
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
    fun `biz update lot repo test not found`() = notFoundErrorTest(StubValues.lots.first(), command)
}