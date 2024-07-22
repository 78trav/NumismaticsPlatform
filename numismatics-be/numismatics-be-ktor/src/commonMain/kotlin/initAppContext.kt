package ru.numismatics.platform.app.ktor

import io.ktor.server.application.*
import ru.numismatics.backend.biz.BizProcessor
import ru.numismatics.backend.common.context.CorSettings
import ru.numismatics.backend.common.context.AppContext
import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.platform.app.ktor.base.KtorWsSessionRepo

fun Application.initAppContext(apiVersion: ApiVersion): AppContext {
    val corSettings = CorSettings(
        wsSessions = KtorWsSessionRepo()
    )
    return AppContext(
        apiVersion = apiVersion,
        urls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
//        processor = StubProcessor(corSettings = corSettings)
        processor = BizProcessor(corSettings = corSettings)
    )
}
