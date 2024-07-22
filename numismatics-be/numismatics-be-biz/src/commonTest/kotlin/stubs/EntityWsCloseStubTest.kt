package ru.numismatics.backend.biz.test.stubs

import kotlinx.coroutines.test.runTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import kotlin.test.Test

class EntityWsCloseStubTest {

    private val command = Command.WS_CLOSE

    @Test
    fun `ws close country all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.COUNTRY).wrongCommand()
    }

    @Test
    fun `ws close material all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MATERIAL).wrongCommand()
    }

    @Test
    fun `ws close section all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.SECTION).wrongCommand()
    }

    @Test
    fun `ws close lot all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.LOT).wrongCommand()
    }

    @Test
    fun `ws close market price all stubs`() = runTest {
        EntityCommandStubTest(command, EntityType.MARKET_PRICE).wrongCommand()
    }

}
