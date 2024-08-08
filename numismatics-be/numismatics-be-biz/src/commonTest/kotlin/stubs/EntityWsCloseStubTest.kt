package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import kotlin.test.Test

class EntityWsCloseStubTest {

    private val command = Command.WS_CLOSE

    @Test
    fun `ws close lot all stubs`() = runTest {
        EntityCommandStubTest(command).wrongCommand()
    }
}