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
import ru.numismatics.backend.common.repo.base.DbEntityResponseError
import ru.numismatics.backend.common.repo.errorNotFound
import validations.lot.runBizTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

fun notFoundErrorTest(lot: Lot, command: Command) = runBizTest {
    // given
    val processor = BizProcessor(
        CorSettings(
            repo = mapOf(
                RequestType.TEST to MockRepo { DbEntityResponseError(lot, errorNotFound(lot)) }
            )
        )
    )

    val context = NumismaticsPlatformContext(
        command = command,
        state = State.NONE,
        timeStart = Clock.System.now(),
        requestType = RequestType.TEST,
        entityRequest = lot
    )

    // when
    processor.exec(context)

    // then
    assertEquals(State.FAILING, context.state)
    assertTrue(context.entityResponse.isEmpty())
    assertNotNull(context.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}