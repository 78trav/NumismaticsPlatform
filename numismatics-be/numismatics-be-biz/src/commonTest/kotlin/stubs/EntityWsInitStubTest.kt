package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import kotlin.test.Test

class EntityWsInitStubTest {

    private val command = Command.WS_INIT

    @Test
    fun `ws init country all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).wrongCommand()
    }

    @Test
    fun `ws init material all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).wrongCommand()
    }

    @Test
    fun `ws init section all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).wrongCommand()
    }

    @Test
    fun `ws init lot all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).wrongCommand()
    }

    @Test
    fun `ws init market price all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).wrongCommand()
    }

}
