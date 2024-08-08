package ru.numismatics.platform.app.ktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.common.context.AppContext
import ru.numismatics.backend.common.models.core.ApiVersion
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.platform.app.ktor.v2.lot

fun Application.version2Module(
    appContext: AppContext<Lot> = initAppContext(ApiVersion.V2)
) {

    install(ContentNegotiation) {
        json(v2Mapper)
    }

    routing {

        get("/") {
            call.respondText("Welcome to Numismatics Platform (native)!")
        }

        lot(appContext)

    }
}
