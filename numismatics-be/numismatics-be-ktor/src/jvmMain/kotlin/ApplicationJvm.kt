package ru.numismatics.platform.app.ktor.jvm

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.numismatics.backend.api.v1.v1Mapper
import ru.numismatics.backend.common.context.AppContext
import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.platform.app.ktor.commonModule
import ru.numismatics.platform.app.ktor.initAppContext
import ru.numismatics.platform.app.ktor.jvm.v1.lot
import ru.numismatics.platform.app.ktor.jvm.v1.marketPrice
import ru.numismatics.platform.app.ktor.jvm.v1.reference

// function with config (application.conf)
//fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.moduleJvm(
    appContext: AppContext = initAppContext(ApiVersion.V1)
) {

    commonModule()

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
//    install(CallLogging) {
//        level = Level.INFO
//    }

    // Неофициальное задание. Попробуйте сделать этот код работающим
//    val rabbitServer = RabbitApp(appSettings, this@moduleJvm)
//    rabbitServer?.start()

    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(v1Mapper))
        jackson {
//            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            setConfig(v1Mapper.serializationConfig)
            setConfig(v1Mapper.deserializationConfig)
        }
    }

    routing {

        get("/") {
            call.respondText("Welcome to Numismatics Platform (jvm)!")
        }

        reference(appContext)

        lot(appContext)

        marketPrice(appContext)

    }
}
