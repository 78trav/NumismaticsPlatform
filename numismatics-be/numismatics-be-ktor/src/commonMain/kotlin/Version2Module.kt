package ru.numismatics.platform.app.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.common.AppContext
import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.platform.app.ktor.v2.lot
import ru.numismatics.platform.app.ktor.v2.marketPrice
import ru.numismatics.platform.app.ktor.v2.reference

fun Application.version2Module(
    appContext: AppContext = initAppContext(ApiVersion.V2)
) {

    install(ContentNegotiation) {
        json(v2Mapper)
    }

    routing {

        get("/") {
            call.respondText("Welcome to Numismatics Platform (native)!")
        }

        reference(appContext)

        lot(appContext)

        marketPrice(appContext)

    }
}
