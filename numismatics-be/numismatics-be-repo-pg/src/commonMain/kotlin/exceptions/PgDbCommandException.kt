package ru.numismatics.backend.repo.pg.exceptions

class PgDbCommandException(command: String) : PgDbException("DB error: $command statement returned empty result")