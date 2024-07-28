package ru.numismatics.platform.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.numismatics.backend.common.AppContext
import kotlin.reflect.KClass
import ru.numismatics.backend.api.references.fromTransport
import ru.numismatics.backend.api.references.toTransport
import ru.numismatics.backend.api.references.v2ResponseSerialize
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.common.controllerHelper
import ru.numismatics.platform.app.ktor.ws.wsHandler

@OptIn(InternalSerializationApi::class)
private suspend inline fun <reified Q : IReferenceRequest, reified R : IReferenceResponse> ApplicationCall.refProcess(
    appContext: AppContext,
    clazz: KClass<*>,
    logId: String
) {
    appContext.controllerHelper(
        {
            val request = this@refProcess.receiveText()
            println(request)
            val obj = v2Mapper.decodeFromString(Q::class.serializer(), request)
//            val obj = this@refProcess.receive<Q>()
            fromTransport(obj)
        },
        {
            this@refProcess.respond(toTransport() as R)
        },
        clazz,
        logId
    )
}

private suspend fun ApplicationCall.refCreate(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::refCreate::class
    refProcess<ReferenceCreateRequest, ReferenceCreateResponse>(
        appContext,
        cl,
        "${appContext.apiVersion.name}-ref-create"
    )
}

private suspend fun ApplicationCall.refRead(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::refRead::class
    refProcess<ReferenceReadRequest, ReferenceReadResponse>(appContext, cl, "${appContext.apiVersion.name}-ref-read")
}

private suspend fun ApplicationCall.refUpdate(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::refUpdate::class
    refProcess<ReferenceUpdateRequest, ReferenceUpdateResponse>(
        appContext,
        cl,
        "${appContext.apiVersion.name}-ref-update"
    )
}

private suspend fun ApplicationCall.refDelete(appContext: AppContext) {
    val cl: KClass<*> = ApplicationCall::refDelete::class
    refProcess<ReferenceDeleteRequest, ReferenceDeleteResponse>(
        appContext,
        cl,
        "${appContext.apiVersion.name}-ref-delete"
    )
}

internal fun Route.reference(appContext: AppContext) {
    route("ref") {
        post("create") {
            call.refCreate(appContext)
        }
        post("read") {
            call.refRead(appContext)
        }
        post("update") {
            call.refUpdate(appContext)
        }
        post("delete") {
            call.refDelete(appContext)
        }

        webSocket("/ws") {
            wsHandler(
                appContext,
                {
                    fromTransport(v2Mapper.decodeFromString<IReferenceRequest>(it))
                },
                {
                    v2ResponseSerialize(toTransport())
                }
            )
        }
    }
}