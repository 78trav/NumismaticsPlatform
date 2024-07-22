package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import kotlin.test.Test

class EntitySearchStubTest {

    private val command = Command.SEARCH

    @Test
    fun `search country all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).wrongCommand()
    }

    @Test
    fun `search material all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).wrongCommand()
    }

    @Test
    fun `search section all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).wrongCommand()
    }

    @Test
    fun `search lot success`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).successCommand()
    }

    @Test
    fun `search lot other stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).otherCommand()
    }

    @Test
    fun `search market price all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).wrongCommand()
    }

}
