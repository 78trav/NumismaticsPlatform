package ru.numismatics.platform.app.ktor.config

import io.ktor.server.application.*
import ru.numismatics.platform.app.ktor.CONFIG_PATH
import ru.numismatics.platform.app.ktor.getEnvironmentConfig

data class PostgresConfig(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "pg2k24",
    val database: String = "numismatics",
    val schema: String = "public"
) {
    constructor(app: Application) : this(
        host = app.getEnvironmentConfig("$PATH.host", "localhost"),
        port = app.getEnvironmentConfig("$PATH.port", "5432").toInt(),
        user = app.getEnvironmentConfig("$PATH.user", "postgres"),
        password = app.getEnvironmentConfig("$PATH.password", "pg2k24"),
        database = app.getEnvironmentConfig("$PATH.database", "numismatics"),
        schema = app.getEnvironmentConfig("$PATH.schema", "public")
    )

    companion object {
        const val PATH = "$CONFIG_PATH.pg"
    }
}