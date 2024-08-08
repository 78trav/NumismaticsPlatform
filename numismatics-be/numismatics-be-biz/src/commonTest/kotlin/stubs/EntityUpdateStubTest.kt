package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import kotlin.test.Test

class EntityUpdateStubTest {

    private val command = Command.UPDATE

    @Test
    fun `update lot success`() = runTest {
        EntityCommandStubTest(command).successCommand()
    }

    @Test
    fun `update lot other stubs`() = runTest {
        EntityCommandStubTest(command).otherCommand()
    }
}