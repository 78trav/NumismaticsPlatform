package ru.numismatics.platform.app.ktor

import io.ktor.server.application.*
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.repo.base.IRepo

actual fun Application.getDatabaseConfig(type: RequestType): IRepo<Lot> {
    require(type != RequestType.STUB)
    val dbSettingPath = "$CONFIG_PATH.${type.configName}"
    return when (val dbSetting = getEnvironmentConfig(dbSettingPath, "").lowercase()) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set (current: $dbSetting) in application.yaml to one of: " +
                    "'inMemory', 'postgres'"
        )
    }
}