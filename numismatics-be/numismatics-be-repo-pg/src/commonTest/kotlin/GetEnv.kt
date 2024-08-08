package ru.numismatics.backend.repo.pg.test

import ru.numismatics.backend.repo.pg.PgProperties

expect fun getEnv(name: String): String?

expect val properties: PgProperties

fun getPgProperties(port: String?) =
    PgProperties(
        host = "localhost",
        port = port?.toIntOrNull() ?: 5432,
        user = "postgres",
        password = "pg2k24",
        database = "numismatics",
        schema = "public",
        table = "lots"
    )