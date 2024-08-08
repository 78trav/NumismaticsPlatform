package ru.numismatics.backend.common.repo

import ru.numismatics.backend.common.helpers.errorSystem
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.repo.exceptions.RepoConcurrencyException
import ru.numismatics.backend.common.repo.exceptions.RepoException

private const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(entity: Entity) = Error(
    code = "$ERROR_GROUP_REPO-not-found",
    group = ERROR_GROUP_REPO,
    field = "id",
    message = "${entity::class.simpleName} with id ${entity.id()} is not found"
)

fun errorEmptyId() = Error(
    code = "$ERROR_GROUP_REPO-empty-id",
    group = ERROR_GROUP_REPO,
    field = "id",
    message = "Id must not be null or blank"
)

fun errorRepoConcurrency(
    oldEntity: Entity,
    expectedLock: LockId,
    exception: Exception = RepoConcurrencyException(
        id = oldEntity.id(),
        expectedLock = expectedLock,
        actualLock = oldEntity.lock,
    ),
) = Error(
    code = "$ERROR_GROUP_REPO-concurrency",
    group = ERROR_GROUP_REPO,
    field = "lock",
    message = "The ${oldEntity::class.simpleName} with id ${oldEntity.id()} has been changed concurrently by another user or process",
    exception = exception
)

fun errorEmptyLock(entity: Entity) = Error(
    code = "$ERROR_GROUP_REPO-lock-empty",
    group = ERROR_GROUP_REPO,
    field = "lock",
    message = "Lock for ${entity::class.simpleName} with id ${entity.id()} is empty that is not admitted"
)

fun errorDb(exception: RepoException) = errorSystem(
    violationCode = "db-error",
    exception = exception
)
