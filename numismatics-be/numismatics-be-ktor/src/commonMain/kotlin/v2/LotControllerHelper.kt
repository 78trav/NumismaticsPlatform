package ru.numismatics.platform.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.reflect.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.numismatics.backend.api.v2.fromTransport
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.api.v2.models.*
import ru.numismatics.backend.api.v2.toTransport
import ru.numismatics.backend.api.v2.v2ResponseSerialize
import ru.numismatics.backend.common.controllerHelper
import ru.numismatics.backend.common.AppContext
import ru.numismatics.platform.app.ktor.ws.wsHandler
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
private suspend inline fun <reified Q : ILotRequest, reified R : ILotResponse> ApplicationCall.lotProcess(
    appContext: AppContext,
    clazz: KClass<*>,
    logId: String
) {
    appContext.controllerHelper(
        {
            val request = this@lotProcess.receiveText()
            println(request)
            val obj = v2Mapper.decodeFromString(Q::class.serializer(), request)
//            val obj = this@lotProcess.receive<Q>()
            fromTransport(obj)
        },
        {
            val obj = toTransport()
            this@lotProcess.respond(obj as R)
        },
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
    route("v2") {
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
                    val obj = v2Mapper.decodeFromString<ILotRequest>(it)
                    fromTransport(obj)
                },
                {
                    val obj = toTransport()
                    v2ResponseSerialize(obj)
                }
            )
        }

    }
}