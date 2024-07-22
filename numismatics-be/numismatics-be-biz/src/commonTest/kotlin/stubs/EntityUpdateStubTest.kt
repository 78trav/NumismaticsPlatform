package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import kotlin.test.Test

class EntityUpdateStubTest {

    private val command = Command.UPDATE

    @Test
    fun `update country success`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).successCommand()
    }

    @Test
    fun `update country other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).otherCommand()
    }

    @Test
    fun `update material success`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).successCommand()
    }

    @Test
    fun `update material other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).otherCommand()
    }

    @Test
    fun `update section success`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).successCommand()
    }

    @Test
    fun `update section other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).otherCommand()
    }

    @Test
    fun `update lot success`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).successCommand()
    }

    @Test
    fun `update lot other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).otherCommand()
    }

    @Test
    fun `update market price all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).wrongCommand()
    }
}
