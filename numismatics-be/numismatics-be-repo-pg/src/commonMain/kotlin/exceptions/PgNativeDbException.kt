package ru.numismatics.backend.repo.pg.exceptions

import ru.numismatics.backend.common.repo.exceptions.RepoException

class PgNativeDbException(msg: String) : RepoException(msg)