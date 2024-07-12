package ru.numismatics.platform.app.ktor.test.post.mp

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.*
import ru.numismatics.backend.api.marketprice.models.*
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.common.models.entities.asString
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.app.ktor.test.post.TestApplicationV1
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class V1StubMarketPriceTest() : TestApplicationV1("marketPrice") {

    private val debug = Debug(
        mode = RequestDebugMode.STUB,
        stub = RequestDebugStubs.SUCCESS
    )

    private val converter = KotlinxWebsocketSerializationConverter(v2Mapper)

    @Test
    fun `CREATE market price`() {
        // given
        // when
        postTest(
            func = "create",
            request = MarketPriceCreateRequest(
                lot = MarketPriceCreateObject(1, MarketPrice("20240707", 2f)),
                debug = debug
            ),
        ) { response ->
            // then
            checkHttpResponse(response)
        }
    }

    @Test
    fun `READ market price`() {
        // given
        // when
        postTest(
            func = "read",
            request = MarketPriceReadRequest(
                lot = MarketPriceReadObject(id = 1),
                debug = debug
            ),
        ) { response ->
            // then
            checkHttpResponse(response)
        }
    }


    @Test
    fun `DELETE market price`() {
        // given
        // when
        postTest(
            func = "delete",
            request = MarketPriceDeleteRequest(
                lot = MarketPriceDeleteObject(),
                debug = debug
            ),
        ) { response ->
            // then
            checkHttpResponse(response)
        }
    }

    @Test
    fun `CREATE market price WS`() {

        // given
        val request = MarketPriceCreateRequest(
            lot = MarketPriceCreateObject(1, MarketPrice("20240707", 2f)),
            debug = debug
        )

        // when
        wsTest<IMarketPriceRequest, MarketPriceResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response.marketPrice != null)

            checkMarketPrice(response)
        }
    }

    @Test
    fun `READ market price WS`() {

        // given
        val request = MarketPriceReadRequest(
            lot = MarketPriceReadObject(id = 1),
            debug = debug
        )

        // when
        wsTest<IMarketPriceRequest, MarketPriceResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response.marketPrice != null)

            checkMarketPrice(response)
        }
    }

    @Test
    fun `DELETE market price WS`() {

        // given
        val request = MarketPriceDeleteRequest(
            lot = MarketPriceDeleteObject(),
            debug = debug
        )

        // when
        wsTest<IMarketPriceRequest, MarketPriceResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response.marketPrice != null)

            checkMarketPrice(response)
        }
    }

    private suspend fun checkHttpResponse(response: HttpResponse) {

        assertEquals(200, response.status.value)

        val responseObj = response.body<MarketPriceResponse>()
        println(responseObj.toString())

        checkMarketPrice(responseObj)
    }

    private fun checkMarketPrice(response: MarketPriceResponse) {

        val mpInt = StubProcessor.lots.first()

        assertEquals(mpInt.marketPrice.size, response.marketPrice?.size)

        mpInt.marketPrice.forEachIndexed { index, marketPrice ->
            val mpExt = response.marketPrice?.get(index)

            assertEquals(marketPrice.date.asString(), mpExt?.date)
            assertEquals(marketPrice.amount, mpExt?.amount)
        }

    }

}
