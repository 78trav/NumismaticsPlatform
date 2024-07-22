package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import kotlin.test.Test

class EntityDeleteStubTest {

    private val command = Command.DELETE

    @Test
    fun `delete country success`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).successCommand()
    }

    @Test
    fun `delete country other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).otherCommand()
    }

    @Test
    fun `delete material success`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).successCommand()
    }

    @Test
    fun `delete material other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).otherCommand()
    }

    @Test
    fun `delete section success`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).successCommand()
    }

    @Test
    fun `delete section other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).otherCommand()
    }

    @Test
    fun `delete lot success`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).successCommand()
    }

    @Test
    fun `delete lot other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).otherCommand()
    }

    @Test
    fun `delete market price success`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).successCommand()
    }

    @Test
    fun `delete market price other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).otherCommand()
    }

}
