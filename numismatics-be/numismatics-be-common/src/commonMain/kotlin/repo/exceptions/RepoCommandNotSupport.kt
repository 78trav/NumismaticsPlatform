package ru.numismatics.backend.common.repo.exceptions

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.entities.Entity

class RepoCommandNotSupport(command: Command, entity: Entity): RepoException("Команда $command для сущности ${entity::class.simpleName} не поддерживается")