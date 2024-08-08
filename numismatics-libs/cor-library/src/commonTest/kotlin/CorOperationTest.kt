package ru.numismatics.platform.libs.cor.test

import kotlinx.coroutines.test.runTest
import ru.numismatics.platform.libs.cor.operation.CorOperation
import ru.numismatics.platform.libs.cor.work.CorJob
import kotlin.test.Test
import kotlin.test.assertEquals

class CorOperationTest {

    @Test
    fun `operation should execute jobs`() = runTest {
        val createJob = { title: String ->
            CorJob<TestContext>(
                name = title,
                blockOn = { status == TestContext.CorStatuses.NONE },
                blockHandle = { history += "$title; " }
            )
        }
        val operation = CorOperation(
            jobs = listOf(createJob("w1"), createJob("w2")),
            blockOn = { true },
            name = "operation",
        )
        val ctx = TestContext()
        operation.exec(ctx)
        assertEquals("w1; w2; ", ctx.history)
    }
}
