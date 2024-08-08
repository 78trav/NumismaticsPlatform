package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import kotlin.test.Test

class EntityCreateStubTest {

    private val command = Command.CREATE

    @Test
    fun `create lot success`() = runTest {
        EntityCommandStubTest(command).successCommand()
    }

    @Test
    fun `create lot other stubs`() = runTest {
        EntityCommandStubTest(command).otherCommand()
    }

}