package ru.numismatics.platform.app.ktor.jvm.v1

//import io.ktor.server.routing.*
//import io.ktor.server.websocket.*
//import ru.numismatics.backend.api.references.fromTransport
//import ru.numismatics.backend.api.references.toTransport
//import ru.numismatics.backend.api.references.v2ResponseSerialize
//import ru.numismatics.backend.api.refs.models.*
//import ru.numismatics.backend.api.v2.mapper.v2Mapper
//import ru.numismatics.backend.common.context.AppContext
//import ru.numismatics.platform.app.ktor.ws.wsHandler
//
//internal fun Route.reference(appContext: AppContext) {
//    route("ref") {
//
//        webSocket("/ws") {
//            wsHandler(
//                appContext,
//                {
//                    fromTransport(v2Mapper.decodeFromString<IReferenceRequest>(it))
//                },
//                {
//                    v2ResponseSerialize(toTransport())
//                }
//            )
//        }
//    }
//}
