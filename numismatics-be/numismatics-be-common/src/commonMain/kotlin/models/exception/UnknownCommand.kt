package ru.numismatics.backend.common.models.exception

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType

class UnknownCommand(command: Command, entityName: String?) :
    Throwable("Wrong command $command with entity ${entityName ?: ""} at mapping toTransport stage")
