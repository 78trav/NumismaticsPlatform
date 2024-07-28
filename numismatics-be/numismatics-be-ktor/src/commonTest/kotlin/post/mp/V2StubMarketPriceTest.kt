package ru.numismatics.platform.app.ktor.test.post.marketPrice

import io.ktor.client.call.*
import io.ktor.client.statement.*
import ru.numismatics.backend.api.marketprice.models.*
import ru.numismatics.backend.common.models.entities.asString
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.app.ktor.test.post.TestApplicationV2
import kotlin.test.Test
import kotlin.test.assertEquals

class V2StubMarketPriceTest() : TestApplicationV2("marketPrice") {

    private val debug = Debug(
        mode = RequestDebugMode.STUB,
        stub = RequestDebugStubs.SUCCESS
    )

    @Test
    fun `create market price`() {
        // given
        // when
        postTest(
            func = "create",
            request = MarketPriceCreateRequest(
                lot = MarketPriceCreateObject(),
                debug = debug
            ),
        ) { response ->
            // then
            checkMarketPrice(response)
        }
    }

    @Test
    fun `read market price`() {
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
            checkMarketPrice(response)
        }
    }


    @Test
    fun `delete market price`() {
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
            checkMarketPrice(response)
        }
    }

    private suspend fun checkMarketPrice(response: HttpResponse) {

        assertEquals(200, response.status.value)

        val responseObj = response.body<MarketPriceResponse>()
        println(responseObj.toString())

        val mpInt = StubProcessor.lots.first()

        assertEquals(mpInt.marketPrice.size, responseObj.marketPrice?.size)

        mpInt.marketPrice.forEachIndexed { index, marketPrice ->
            val mpExt = responseObj.marketPrice?.get(index)

            assertEquals(marketPrice.date.asString(), mpExt?.date)
            assertEquals(marketPrice.amount, mpExt?.amount)
        }
    }
}