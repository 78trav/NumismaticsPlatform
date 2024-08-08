package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import kotlin.test.Test

class EntityReadStubTest {

    private val command = Command.READ

    @Test
    fun `read lot success`() = runTest {
        EntityCommandStubTest(command).successCommand()
    }

    @Test
    fun `read lot other stubs`() = runTest {
        EntityCommandStubTest(command).otherCommand()
    }
}