package ru.numismatics.backend.common.models.exception

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType

class UnknownCommand(command: Command, entityType: EntityType) : Throwable("Wrong command $command with entity '${entityType.description()}' at mapping toTransport stage")
