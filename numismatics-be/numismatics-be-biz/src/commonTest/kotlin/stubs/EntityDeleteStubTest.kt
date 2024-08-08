package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import kotlin.test.Test

class EntityDeleteStubTest {

    private val command = Command.DELETE

    @Test
    fun `delete lot success`() = runTest {
        EntityCommandStubTest(command).successCommand()
    }

    @Test
    fun `delete lot other stubs`() = runTest {
        EntityCommandStubTest(command).otherCommand()
    }

}