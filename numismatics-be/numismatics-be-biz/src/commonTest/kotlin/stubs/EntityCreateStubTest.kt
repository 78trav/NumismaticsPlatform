package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import kotlin.test.Test

class EntityCreateStubTest {

    private val command = Command.CREATE

    @Test
    fun `create country success`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).successCommand()
    }

    @Test
    fun `create country other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).otherCommand()
    }

    @Test
    fun `create material success`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).successCommand()
    }

    @Test
    fun `create material other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).otherCommand()
    }

    @Test
    fun `create section success`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).successCommand()
    }

    @Test
    fun `create section other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).otherCommand()
    }

    @Test
    fun `create lot success`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).successCommand()
    }

    @Test
    fun `create lot other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).otherCommand()
    }

    @Test
    fun `create market price success`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).successCommand()
    }

    @Test
    fun `create market price other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).otherCommand()
    }
}
