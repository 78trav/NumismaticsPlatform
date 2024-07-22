package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import kotlin.test.Test

class EntityReadStubTest {

    private val command = Command.READ

    @Test
    fun `read country success`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).successCommand()
    }

    @Test
    fun `read country other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).otherCommand()
    }

    @Test
    fun `read material success`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).successCommand()
    }

    @Test
    fun `read material other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).otherCommand()
    }

    @Test
    fun `read section success`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).successCommand()
    }

    @Test
    fun `read section other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).otherCommand()
    }

    @Test
    fun `read lot success`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).successCommand()
    }

    @Test
    fun `read lot other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).otherCommand()
    }

    @Test
    fun `read market price success`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).successCommand()
    }

    @Test
    fun `read market price other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).otherCommand()
    }

}
