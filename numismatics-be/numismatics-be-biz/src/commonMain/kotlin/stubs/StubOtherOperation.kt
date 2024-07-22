package ru.numismatics.backend.biz.stubs

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.stubs.toError
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job
import ru.numismatics.platform.libs.cor.operation.operation

internal fun CorOperationDSL<NumismaticsPlatformContext>.stubOtherOperation() = operation {
    name = "Прочие стабы"
    description = "Имитация прочих стабов, отличных от ${Stubs.SUCCESS}"

    on { state == State.RUNNING && requestType == RequestType.STUB }

    Stubs.entries
        .filter { it !in setOf(Stubs.NONE, Stubs.SUCCESS) }
        .forEach {

            job {
                name = "Имитация ошибки ${it.name}"

                on {
                    state == State.RUNNING && stubCase == it && StubProcessor.entitiesCommands[entityType]?.contains(
                        command
                    ) ?: false
                }

                handle {
                    fail(it.toError())
                }
            }
        }

    stubFailJob()

}
