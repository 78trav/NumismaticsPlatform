package ru.numismatics.platform.app.ktor.ws

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.*
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.AppContext
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.controllerHelper
import ru.numismatics.platform.app.ktor.base.KtorWsSession
import kotlin.reflect.KClass

private val wsHandlerClass: KClass<*> = WebSocketSession::wsHandler::class

suspend fun WebSocketSession.wsHandler(
    appContext: AppContext,
    request: NumismaticsPlatformContext.(source: String) -> Unit,
    response: NumismaticsPlatformContext.() -> String
) {
    with(KtorWsSession(this)) {

        // Обновление реестра сессий
        val sessions = appContext.wsSessions
        sessions.add(this)

        // Handle init request
        appContext.controllerHelper(
            {
                command = Command.WS_INIT
                wsSession = this@with
            },
            {
//                println(0)
                val source = response()
                this@wsHandler.send(Frame.Text(source))
                println("web socket init: $source")
            },
            wsHandlerClass,
            "ws${appContext.apiVersion.name}-init"
        )

        // Handle flow
        incoming
            .receiveAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull {
                it.readText()
            }
            .filter { it.isNotEmpty() }
            .map {
                // Handle without flow destruction
                try {
                    appContext.controllerHelper(
                        {
//                            println(1)
                            request(it)
                            println("web socket request: $it")
                            wsSession = this@with
                        },
                        {
//                            println(2)
                            val source = response()
//                            println(source)
//                            println(29)
                            this@wsHandler.send(Frame.Text(source))
                            println("web socket response: $source")
//                        // If change request, response is sent to everyone
//                        outgoing.send(Frame.Text(result))
                        },
                        wsHandlerClass,
                        "ws${appContext.apiVersion.name}-handle"
                    )

                } catch (_: ClosedReceiveChannelException) {
                    println("web socket close 1")
                    sessions.remove(this@with)
                } catch (e: Throwable) {
                    println(e)
                }
            }
            .onCompletion {
//                println(7)
                // Handle finish request
                appContext.controllerHelper(
                    {
                        command = Command.WS_CLOSE
                        wsSession = this@with
                    },
                    { },
                    wsHandlerClass,
                    "ws${appContext.apiVersion.name}-close"
                )
                sessions.remove(this@with)
                println("web socket close 2")
            }
            .collect()
    }
}
