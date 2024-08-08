package ru.numismatics.backend.biz.test.stubs

import ru.numismatics.backend.biz.BizProcessor
import ru.numismatics.backend.common.context.CorSettings
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.wrongCommand
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.stubs.toError
import ru.numismatics.backend.stub.StubValues
import kotlin.test.assertEquals

class EntityCommandStubTest(
    private val command: Command
) {

    private val processor = BizProcessor(CorSettings())

    suspend fun successCommand() {
        // when
        runStubCaseTest(Stubs.SUCCESS) {
            // then
            assertEquals(entityRequest, entityResponse.first())
        }
    }

    suspend fun otherCommand() {
        // given
        Stubs.entries
            .filter { it !in setOf(Stubs.NONE, Stubs.SUCCESS) }
            .forEach { stubCase ->
                // when
                runStubCaseTest(stubCase) {
                    // then
                    println("$stubCase")
                    assertEquals(1, errors.size)
                    assertEquals(stubCase.toError(), errors.first())
                }
            }
    }

    suspend fun wrongCommand() {
        // given
        Stubs.entries
            .filter { it != Stubs.NONE }
            .forEach { stubCase ->
                // when
                runStubCaseTest(stubCase) {
                    // then
                    assertEquals(1, errors.size)
                    assertEquals(wrongCommand(), errors.first())
                }
            }
    }

    private suspend fun runStubCaseTest(stubCase: Stubs, assertBlock: NumismaticsPlatformContext<Lot>.() -> Unit) =
        NumismaticsPlatformContext(
            command = command,
            requestType = RequestType.STUB,
            stubCase = stubCase,
            entityRequest = StubValues.lots.first()
        ).also {
            processor.exec(it)
            it.assertBlock()
        }
}