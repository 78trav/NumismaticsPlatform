package ru.numismatics.platform.app.ktor

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*

fun Application.commonModule() {
    install(CORS) {
        allowMethod(HttpMethod.Post)
//        allowMethod(HttpMethod.Options)
//        allowMethod(HttpMethod.Put)
//        allowMethod(HttpMethod.Delete)
//        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true

        val host = this@commonModule.getEnvironmentConfig("ktor.deployment.host", "127.0.0.1")
        val port = this@commonModule.getEnvironmentConfig("ktor.deployment.port", "8080")

        allowHost("$host:$port")
    }
//    install(Routing)
    install(WebSockets)
//    install(DoubleReceive) {
//        cacheRawRequest = false
//    }
}

fun Application.getEnvironmentConfig(key: String, defaultValue: String) =
    environment.config.propertyOrNull(key)?.getString() ?: defaultValue
