package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import kotlin.test.Test

class EntityWsInitStubTest {

    private val command = Command.WS_INIT

    @Test
    fun `ws init lot all stubs`() = runTest {
        EntityCommandStubTest(command).wrongCommand()
    }
}