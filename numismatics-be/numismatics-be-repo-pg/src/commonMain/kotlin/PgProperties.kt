package ru.numismatics.backend.repo.pg

internal const val DRIVER_PREFIX = "jdbc:postgresql://"

data class PgProperties(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val database: String,
    val schema: String,
    val table: String
) {
    val url: String
        get() = "$DRIVER_PREFIX${host}:${port}/${database}"
}