package ru.numismatics.platform.app.ktor

import io.ktor.server.application.*
import ru.numismatics.backend.common.AppContext
import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.app.ktor.base.KtorWsSessionRepo

fun Application.initAppContext(apiVersion: ApiVersion) = AppContext(
    apiVersion = apiVersion,
    urls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    wsSessions = KtorWsSessionRepo(),
    processor = StubProcessor() //BizProcessor()
)