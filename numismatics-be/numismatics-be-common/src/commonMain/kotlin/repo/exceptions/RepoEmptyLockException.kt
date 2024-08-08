package ru.numismatics.backend.common.repo.exceptions

import ru.numismatics.backend.common.models.id.Identifier

class RepoEmptyLockException(id: Identifier) : RepoEntityException(
    id,
    "Lock is empty in DB"
)
