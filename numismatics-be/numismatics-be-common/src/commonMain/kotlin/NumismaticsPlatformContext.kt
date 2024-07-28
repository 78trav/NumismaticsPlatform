package ru.numismatics.backend.common

import kotlinx.datetime.Instant
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

    var requestType: RequestType = RequestType.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.EMPTY,
    val timeStart: Instant = Instant.NONE,

    var entityType: EntityType = EntityType.UNDEFINED,
    var entityRequest: Entity = EmptyEntity,

    val entityResponse: Entities = mutableListOf(),

    var wsSession: IWsSession = IWsSession.NONE
)