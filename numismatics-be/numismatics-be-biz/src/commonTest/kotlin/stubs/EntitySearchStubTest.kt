package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import kotlin.test.Test

class EntitySearchStubTest {

    private val command = Command.SEARCH

    @Test
    fun `search lot success`() = runTest {
        EntityCommandStubTest(command).successCommand()
    }

    @Test
    fun `search lot other stubs`() = runTest {
        EntityCommandStubTest(command).otherCommand()
    }
}