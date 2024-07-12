package ru.numismatics.platform.app.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.config.yaml.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, environment = applicationEngineEnvironment {
        val conf = YamlConfigLoader().load("./application.yaml")
            ?: throw RuntimeException("Cannot read application.yaml")

        println(conf)

        config = conf

        println("File read")

        module {
            commonModule()
            version2Module()
        }

        connector {
            port = conf.port
            host = conf.host
        }

        println("Starting")

    }).start(true)
}
