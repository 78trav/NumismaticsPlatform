package ru.numismatics.backend.common.repo.exceptions

import ru.numismatics.backend.common.models.id.Identifier
import ru.numismatics.backend.common.models.id.LockId

class RepoConcurrencyException(id: Identifier, expectedLock: LockId, actualLock: LockId?) : RepoEntityException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
