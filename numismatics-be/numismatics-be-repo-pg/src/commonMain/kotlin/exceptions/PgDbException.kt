package ru.numismatics.backend.repo.pg.exceptions

import ru.numismatics.backend.common.repo.exceptions.RepoException

open class PgDbException(msg: String) : RepoException(msg)