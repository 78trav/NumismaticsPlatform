package ru.numismatics.platform.app.ktor.test.post

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.platform.app.ktor.commonModule
import ru.numismatics.platform.app.ktor.version2Module
import kotlin.test.assertIs

abstract class TestApplicationV2(val endPoint: String) {

    protected inline fun <reified T> postTest(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ) {
        testApplication {
            environment {
                if (config is MapApplicationConfig) {
                    config = MapApplicationConfig(
                        listOf(
                            "np.repository.test" to "mem",
                            "np.repository.prod" to "mem"
                        )
                    )
                }
            }

            application {
                commonModule()
                version2Module()
            }
            val client = createClient {
                install(ContentNegotiation) {
                    json(v2Mapper)
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
        request: Q,
        crossinline assertBlock: (response: R) -> Unit
    ) {
        testApplication {
            application {
                commonModule()
                version2Module()
            }

            val client = createClient {
                install(WebSockets) {
                    this.contentConverter = KotlinxWebsocketSerializationConverter(v2Mapper)
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