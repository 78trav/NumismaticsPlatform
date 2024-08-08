package ru.numismatics.platform.app.ktor

import io.ktor.server.application.*
import ru.numismatics.backend.biz.BizProcessor
import ru.numismatics.backend.common.context.CorSettings
import ru.numismatics.backend.common.context.AppContext
import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.repo.base.IRepo
import ru.numismatics.backend.repo.inmemory.InMemoryRepo
import ru.numismatics.backend.repo.pg.PgProperties
import ru.numismatics.backend.repo.pg.PgRepoLot
import ru.numismatics.backend.stub.StubValues
import ru.numismatics.platform.app.ktor.base.KtorWsSessionRepo
import ru.numismatics.platform.app.ktor.config.PostgresConfig
import kotlin.time.Duration

internal const val CONFIG_PATH = "np.repository"

fun Application.initAppContext(apiVersion: ApiVersion): AppContext<Lot> {

    val repo: Map<RequestType, IRepo<Lot>> = mapOf(
        RequestType.TEST to getDatabaseConfig(RequestType.TEST),
        RequestType.PROD to getDatabaseConfig(RequestType.PROD)
    )

    val corSettings = CorSettings(
        wsSessions = KtorWsSessionRepo(),
        repo = repo
    )
    return AppContext(
        apiVersion = apiVersion,
        urls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = BizProcessor(corSettings = corSettings)
    )
}

expect fun Application.getDatabaseConfig(type: RequestType): IRepo<Lot>

fun Application.initInMemory(): IRepo<Lot> {
    val ttl = Duration.parse(getEnvironmentConfig("$CONFIG_PATH.mem.ttl", "10m"))
    return InMemoryRepo(ttl, getInitData("mem"))
}

fun Application.initPostgres(): IRepo<Lot> {
    val pgConfig = PostgresConfig(this)
    return PgRepoLot(
        PgProperties(
            host = pgConfig.host,
            port = pgConfig.port,
            user = pgConfig.user,
            password = pgConfig.password,
            database = pgConfig.database,
            schema = pgConfig.schema,
            table = "lots"
        )
    )
}

private fun Application.getInitData(repo: String) = when (getEnvironmentConfig("$CONFIG_PATH.$repo.stubs", "").lowercase()) {
    "true", "yes" -> StubValues.lots
    else -> emptyList()
}