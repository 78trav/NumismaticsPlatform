package ru.numismatics.backend.biz.test.biz

import kotlinx.datetime.Clock
import ru.numismatics.backend.biz.BizProcessor
import ru.numismatics.backend.common.context.CorSettings
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.repo.inmemory.InMemoryRepo
import ru.numismatics.backend.stub.StubValues
import kotlin.jvm.JvmStatic

abstract class BizValidationBase(protected val command: Command) {

    companion object {
        protected val errorCodes = listOf(
            "validation-name-empty",
            "validation-name-noContent",
            "validation-description-empty",
            "validation-description-noContent",
            "validation-id-empty",
            "validation-lock-empty",
            "validation-lock-badFormat",
            "validation-year-badValue",
            "validation-marketprice-empty",
            "validation-command-badValue"
        )

        @JvmStatic
        protected val badString = "!@#$%^&*(),.{}"

        @JvmStatic
        protected inline fun forTrim(source: String) = " \t \n $source \t\n "
    }

    private val settings by lazy {
        CorSettings(
            repo = mapOf(
                RequestType.TEST to InMemoryRepo(initData = StubValues.lots)
            )
        )
    }
    private val processor by lazy { BizProcessor(settings) }

    suspend fun execTest(
        lot: Lot,
        assertBlock: NumismaticsPlatformContext<Lot>.() -> Unit
    ) {

        // given
        NumismaticsPlatformContext(
            command = command,
            requestType = RequestType.TEST,
            timeStart = Clock.System.now(),
            entityRequest = lot
        ).also { context ->
            // when
            processor.exec(context)
            // then
            context.assertBlock()
        }
    }
}