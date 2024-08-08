package ru.numismatics.platform.libs.cor.test

import kotlinx.coroutines.test.runTest
import ru.numismatics.platform.libs.cor.core.CorExecDSL
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CorDSLTest {

    private suspend fun execute(dsl: CorExecDSL<TestContext>) = TestContext().also { dsl.build().exec(it) }

    @Test
    fun `handle should execute`() = runTest {
        val operation = process<TestContext> {
            job {
                on { true }
                handle { history += "w1; " }
            }
        }
        val ctx = execute(operation)
        assertEquals("w1; ", ctx.history)
    }

    @Test
    fun `on should check condition`() = runTest {
        val operation = process<TestContext> {
            job {
                on { status == TestContext.CorStatuses.ERROR }
                handle { history += "w1; " }
            }
            job {
                on { status == TestContext.CorStatuses.NONE }
                handle {
                    history += "w2; "
                    status = TestContext.CorStatuses.FAILING
                }
            }
            job {
                on { status == TestContext.CorStatuses.FAILING }
                handle { history += "w3; " }
            }
        }
        assertEquals("w2; w3; ", execute(operation).history)
    }

    @Test
    fun `except should execute when exception`() = runTest {
        val message = "some error"
        val operation = process<TestContext> {
            job {
                on { true }
                handle { throw RuntimeException(message) }
                except { history += it.message }
            }
        }
        assertEquals(message, execute(operation).history)
    }

    @Test
    fun `should throw when exception and no except`() = runTest {
        val message = "some error"
        val operation = process<TestContext> {
            job("throw") {
                throw RuntimeException(message)
            }
        }
        assertFails {
            execute(operation)
        }
    }

    @Test
    fun `complex operation example`() = runTest {
        val operation = process<TestContext> {
            job {
                name = "Инициализация статуса"
                description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

                on { status == TestContext.CorStatuses.NONE }
                handle { status = TestContext.CorStatuses.RUNNING }
                except { status = TestContext.CorStatuses.ERROR }
            }

            operation {
                on { status == TestContext.CorStatuses.RUNNING }

                job(
                    name = "Лямбда обработчик",
                    description = "Пример использования обработчика в виде лямбды"
                ) {
                    counter += 4
                }
            }

            printResult()

        }

        val ctx = execute(operation)
        println("Complete: $ctx")
    }

    private fun CorOperationDSL<TestContext>.printResult() = job(name = "Print example") {
        println("counter = $counter")
    }
}
