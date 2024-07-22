package ru.numismatics.platform.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.numismatics.backend.api.marketprice.fromTransport
import ru.numismatics.backend.api.marketprice.models.*
import ru.numismatics.backend.api.marketprice.toTransport
import ru.numismatics.backend.api.marketprice.v2ResponseSerialize
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.common.context.AppContext
import ru.numismatics.backend.common.controllerHelper
import ru.numismatics.platform.app.ktor.ws.wsHandler
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
private suspend inline fun <reified Q : IMarketPriceRequest> ApplicationCall.marketPriceProcess(
    appContext: AppContext,
    clazz: KClass<*>,
    logId: String
) {
    appContext.controllerHelper(
        {
            val request = this@marketPriceProcess.receiveText()
            println(request)
            val obj = v2Mapper.decodeFromString(Q::class.serializer(), request)
//            val obj = this@marketPriceProcess.receive<Q>()
            fromTransport(obj)
        },
        { this@marketPriceProcess.respond(toTransport()) },
        clazz,
        logId
    )
}

private suspend fun ApplicationCall.marketPriceCreate(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::marketPriceCreate::class
    marketPriceProcess<MarketPriceCreateRequest>(appContext, cl, "${appContext.apiVersion.name}-mp-create")
}

private suspend fun ApplicationCall.marketPriceRead(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::marketPriceRead::class
    marketPriceProcess<MarketPriceReadRequest>(appContext, cl, "${appContext.apiVersion.name}-mp-read")
}

private suspend fun ApplicationCall.marketPriceDelete(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::marketPriceDelete::class
    marketPriceProcess<MarketPriceDeleteRequest>(appContext, cl, "${appContext.apiVersion.name}-mp-delete")
}

internal fun Route.marketPrice(appContext: AppContext) {
    route("marketPrice") {
        post("create") {
            call.marketPriceCreate(appContext)
        }
        post("read") {
            call.marketPriceRead(appContext)
        }
        post("delete") {
            call.marketPriceDelete(appContext)
        }

        webSocket("/ws") {
            wsHandler(
                appContext,
                {
                    fromTransport(v2Mapper.decodeFromString<IMarketPriceRequest>(it))
                },
                {
                    v2ResponseSerialize(toTransport())
                }
            )
        }
    }
}
