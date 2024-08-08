package ru.numismatics.backend.api.v2.test

import ru.numismatics.backend.api.v2.models.Debug
import ru.numismatics.backend.api.v2.models.RequestDebugMode
import ru.numismatics.backend.api.v2.models.RequestDebugStubs
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.stub.StubValues

abstract class TestValues {

    companion object {

        val debug = Debug(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.SUCCESS
        )

        val filledContext = NumismaticsPlatformContext(
            state = State.RUNNING,
            errors = mutableListOf(StubValues.error),
            requestType = RequestType.TEST,
            requestId = RequestId("832"),
            entityRequest = Lot.EMPTY,
            entityResponse = mutableListOf(StubValues.lots.first())
        )
    }
}
