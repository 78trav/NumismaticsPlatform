package ru.numismatics.backend.common.repo.exceptions

import ru.numismatics.backend.common.models.id.Identifier

open class RepoEntityException(
    val id: Identifier,
    msg: String,
) : RepoException(msg)
