package ru.numismatics.backend.common.context

import kotlinx.datetime.Instant
import ru.numismatics.backend.common.NONE
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.models.entities.EmptyEntity
import ru.numismatics.backend.common.models.entities.Entities
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.models.id.RequestId
import ru.numismatics.backend.common.ws.IWsSession

data class NumismaticsPlatformContext(

    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: Errors = mutableListOf(),

    var corSettings: CorSettings = CorSettings(),

    var requestType: RequestType = RequestType.TEST,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.EMPTY,
    val timeStart: Instant = Instant.NONE,

    var entityType: EntityType = EntityType.UNDEFINED,
    var entityRequest: Entity = EmptyEntity,

    var entityValidating: Entity = EmptyEntity,
    var entityValidated: Entity = EmptyEntity,

    val entityResponse: Entities = mutableListOf(),

    var wsSession: IWsSession = IWsSession.NONE
)

inline fun NumismaticsPlatformContext.addError(error: Error) = errors.add(error)
inline fun NumismaticsPlatformContext.addErrors(error: Collection<Error>) = errors.addAll(error)

inline fun NumismaticsPlatformContext.fail(error: Error) {
    addError(error)
    state = State.FAILING
}

inline fun NumismaticsPlatformContext.fail(errors: Collection<Error>) {
    addErrors(errors)
    state = State.FAILING
}

fun NumismaticsPlatformContext.wrongCommand() =
    Error(
        code = "validation-command-badValue",
        group = "validation",
        message = "Команда $command для сущности $entityType не поддерживается"
    )
