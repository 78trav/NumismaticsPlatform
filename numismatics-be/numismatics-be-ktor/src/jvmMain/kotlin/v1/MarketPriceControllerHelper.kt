package ru.numismatics.platform.app.ktor.jvm.v1

//import io.ktor.server.application.*
//import io.ktor.server.request.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import io.ktor.server.websocket.*
//import ru.numismatics.backend.api.marketprice.fromTransport
//import ru.numismatics.backend.api.marketprice.models.IMarketPriceRequest
//import ru.numismatics.backend.api.marketprice.models.MarketPriceCreateRequest
//import ru.numismatics.backend.api.marketprice.models.MarketPriceDeleteRequest
//import ru.numismatics.backend.api.marketprice.models.MarketPriceReadRequest
//import ru.numismatics.backend.api.marketprice.toTransport
//import ru.numismatics.backend.api.marketprice.v2ResponseSerialize
//import ru.numismatics.backend.api.v1.v1Mapper
//import ru.numismatics.backend.api.v2.mapper.v2Mapper
//import ru.numismatics.backend.common.context.AppContext
//import ru.numismatics.backend.common.controllerHelper
//import ru.numismatics.platform.app.ktor.ws.wsHandler
//import kotlin.reflect.KClass
//
//private suspend inline fun <reified Q : IMarketPriceRequest> ApplicationCall.marketPriceProcess(
//    appContext: AppContext,
//    clazz: KClass<*>,
//    logId: String
//) {
//    appContext.controllerHelper(
//        {
//            val request = this@marketPriceProcess.receiveText()
//            println(request)
//
//            val obj = v1Mapper.readValue(request, Q::class.java)
//            fromTransport(obj)
//
////            fromTransport(this@marketPriceProcess.receive<Q>())
//        },
//        { this@marketPriceProcess.respond(toTransport()) },
//        clazz,
//        logId
//    )
//}
//
//private suspend fun ApplicationCall.marketPriceCreate(appContext: AppContext) {
//    val cl: KClass<*> = ApplicationCall::marketPriceCreate::class
//    marketPriceProcess<MarketPriceCreateRequest>(appContext, cl, "${appContext.apiVersion.name}-mp-create")
//}
//
//private suspend fun ApplicationCall.marketPriceRead(appContext: AppContext) {
//    val cl: KClass<*> = ApplicationCall::marketPriceRead::class
//    marketPriceProcess<MarketPriceReadRequest>(appContext, cl, "${appContext.apiVersion.name}-mp-read")
//}
//
//private suspend fun ApplicationCall.marketPriceDelete(appContext: AppContext) {
//    val cl: KClass<*> = ApplicationCall::marketPriceDelete::class
//    marketPriceProcess<MarketPriceDeleteRequest>(appContext, cl, "${appContext.apiVersion.name}-mp-delete")
//}
//
//internal fun Route.marketPrice(appContext: AppContext) {
//    route("marketPrice") {
//        post("create") {
//            call.marketPriceCreate(appContext)
//        }
//        post("read") {
//            call.marketPriceRead(appContext)
//        }
//        post("delete") {
//            call.marketPriceDelete(appContext)
//        }
//
//        webSocket("/ws") {
//            wsHandler(
//                appContext,
//                {
//                    fromTransport(v2Mapper.decodeFromString<IMarketPriceRequest>(it))
//                },
//                {
//                    v2ResponseSerialize(toTransport())
//                }
//            )
//        }
//
//    }
//}
