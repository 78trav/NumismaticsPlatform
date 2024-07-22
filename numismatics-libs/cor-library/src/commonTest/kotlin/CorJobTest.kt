package ru.numismatics.platform.libs.cor.test

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import ru.numismatics.platform.libs.cor.work.CorJob

class CorJobTest {

    @Test
    fun `job should execute handle`() = runTest {
        val job = CorJob<TestContext>(
            name = "w1",
            blockOn = { true },
            blockHandle = { counter += 1 }
        )
        val ctx = TestContext()
        job.exec(ctx)
        assertEquals(1, ctx.counter)
    }

    @Test
    fun `job should not execute when off`() = runTest {
        val job = CorJob<TestContext>(
            name = "w2",
            blockOn = { status == TestContext.CorStatuses.ERROR },
            blockHandle = { counter += 1 }
        )
        val ctx = TestContext()
        job.exec(ctx)
        assertEquals(0, ctx.counter)
    }

    @Test
    fun `job should handle exception`() = runTest {
        val message = "some error"
        val job = CorJob<TestContext>(
            name = "w3",
            blockOn = { true },
            blockHandle = { throw RuntimeException(message) },
            blockExcept = { e -> history += e.message }
        )
        val ctx = TestContext()
        job.exec(ctx)
        assertEquals(message, ctx.history)
    }

}
