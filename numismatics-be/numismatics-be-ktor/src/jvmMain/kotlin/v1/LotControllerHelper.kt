package ru.numismatics.platform.app.ktor.jvm.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.numismatics.backend.api.v1.fromTransport
import ru.numismatics.backend.api.v1.models.*
import ru.numismatics.backend.api.v1.toTransport
import ru.numismatics.backend.common.AppContext
import ru.numismatics.backend.common.controllerHelper
import ru.numismatics.backend.api.v1.v1Mapper
import ru.numismatics.backend.api.v1.v1ResponseSerialize
import ru.numismatics.platform.app.ktor.ws.wsHandler
import kotlin.reflect.KClass

private suspend inline fun <reified Q : ILotRequest, reified R : ILotResponse> ApplicationCall.lotProcess(
    appContext: AppContext,
    clazz: KClass<*>,
    logId: String
) {
    appContext.controllerHelper(
        {
            val request = this@lotProcess.receiveText()
            println(request)

            val obj = v1Mapper.readValue(request, Q::class.java)
            fromTransport(obj)

//            fromTransport(this@lotProcess.receive<Q>())
        },
        { this@lotProcess.respond(toTransport() as R) },
        clazz,
        logId
    )
}

private suspend fun ApplicationCall.lotCreate(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::lotCreate::class



    lotProcess<LotCreateRequest, LotCreateResponse>(appContext, cl, "${appContext.apiVersion.name}-lot-create")
}

private suspend fun ApplicationCall.lotRead(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::lotRead::class
    lotProcess<LotReadRequest, LotReadResponse>(appContext, cl, "${appContext.apiVersion.name}-lot-read")
}

private suspend fun ApplicationCall.lotUpdate(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::lotUpdate::class
    lotProcess<LotUpdateRequest, LotUpdateResponse>(appContext, cl, "${appContext.apiVersion.name}-lot-update")
}

private suspend fun ApplicationCall.lotDelete(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::lotDelete::class
    lotProcess<LotDeleteRequest, LotDeleteResponse>(appContext, cl, "${appContext.apiVersion.name}-lot-delete")
}

private suspend fun ApplicationCall.lotSearch(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::lotSearch::class
    lotProcess<LotSearchRequest, LotSearchResponse>(appContext, cl, "${appContext.apiVersion.name}-lot-search")
}

internal fun Route.lot(appContext: AppContext) {
    route("v1") {
        post("create") {
            call.lotCreate(appContext)
        }
        post("read") {
            call.lotRead(appContext)
        }
        post("update") {
            call.lotUpdate(appContext)
        }
        post("delete") {
            call.lotDelete(appContext)
        }
        post("search") {
            call.lotSearch(appContext)
        }

        webSocket("/ws") {

            wsHandler(
                appContext,
                {
//                    println(11)
                    val obj = v1Mapper.readValue(it, ILotRequest::class.java)
//                    println(12)
                    fromTransport(obj)
//                    println(13)
//                    println(obj)
//                    println(14)
                },
                {
//                    println(22)
                    val obj = toTransport()
//                    println(obj)
//                    println(23)
                    v1ResponseSerialize(obj)
                }
            )
        }
    }
}
