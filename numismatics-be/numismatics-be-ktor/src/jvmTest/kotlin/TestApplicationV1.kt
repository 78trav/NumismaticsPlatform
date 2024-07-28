package ru.numismatics.platform.app.ktor.test.post

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.numismatics.platform.app.ktor.jvm.moduleJvm
import ru.numismatics.backend.api.v1.v1Mapper
import kotlin.test.assertIs

abstract class TestApplicationV1(val endPoint: String) {

    protected inline fun <reified T> postTest(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ) {
        testApplication {
            application {
                moduleJvm()
            }
            val client = createClient {
                install(ContentNegotiation) {
                    register(ContentType.Application.Json, JacksonConverter(v1Mapper))
                    jackson {
//                        setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        setConfig(v1Mapper.serializationConfig)
                        setConfig(v1Mapper.deserializationConfig)
                    }
                }
            }
            val response = client.post("/$endPoint/$func") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            function(response)
        }
    }

    protected inline fun <reified Q, reified R> wsTest(
        contentConverter: WebsocketContentConverter,
        request: Q,
        crossinline assertBlock: (response: R) -> Unit
    ) {
        testApplication {
            application {
                moduleJvm()
            }

            val client = createClient {
                install(WebSockets) {
                    this.contentConverter = contentConverter
                }
            }

            client.webSocket("/$endPoint/ws") {
                withTimeout(3000) {
                    val response = receiveDeserialized<R>()
                    assertIs<R>(response)
                }
                sendSerialized<Q>(request)
                withTimeout(3000) {
                    val response = receiveDeserialized<R>()
                    assertBlock(response)
                }
            }
        }
    }
}