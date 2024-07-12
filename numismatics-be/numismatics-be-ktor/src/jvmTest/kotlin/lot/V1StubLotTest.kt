package ru.numismatics.platform.app.ktor.test.post.lot

import io.ktor.client.call.*
import io.ktor.serialization.jackson.*
import ru.numismatics.backend.api.v1.models.*
import ru.numismatics.backend.api.v1.models.Condition
import ru.numismatics.backend.api.v1.models.EntityPermission
import ru.numismatics.backend.api.v1.v1Mapper
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.app.ktor.test.post.TestApplicationV1
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class V1StubLotTest : TestApplicationV1("v1") {

    private val debug = Debug(
        mode = RequestDebugMode.STUB,
        stub = RequestDebugStubs.SUCCESS
    )

    private val converter = JacksonWebsocketContentConverter(v1Mapper)

    @Test
    fun `CREATE lot`() {
        // given
        // when
        postTest(
            func = "create",
            request = LotCreateRequest(
                lot = LotCreateObject(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotCreateResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lot
            assertTrue(refExt is LotResponse)

            checkLot(refExt)
        }
    }

    @Test
    fun `READ lot`() {
        // given
        // when
        postTest(
            func = "read",
            request = LotReadRequest(
                lot = LotReadObject(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotReadResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lot
            assertTrue(refExt is LotResponse)

            checkLot(refExt)
        }
    }

    @Test
    fun `UPDATE lot`() {
        // given
        // when
        postTest(
            func = "update",
            request = LotUpdateRequest(
                lot = LotUpdateObject(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotUpdateResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lot
            assertTrue(refExt is LotResponse)

            checkLot(refExt)
        }
    }

    @Test
    fun `DELETE lot`() {
        // given
        // when
        postTest(
            func = "delete",
            request = LotDeleteRequest(
                lot = LotDeleteObject(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotDeleteResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lot
            assertTrue(refExt is LotResponse)

            checkLot(refExt)
        }
    }

    @Test
    fun `SEARCH lots`() {
        // given
        // when
        postTest(
            func = "search",
            request = LotSearchRequest(
                filter = LotSearchFilter(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotSearchResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lots?.first()
            assertTrue(refExt is LotResponse)

            checkLot(refExt)
        }
    }


    @Test
    fun `CREATE lot WS`() {

        // given
        val request = LotCreateRequest(
            lot = LotCreateObject(name = "test"),
            debug = debug
        )

        // when
        wsTest<ILotRequest, ILotResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is LotCreateResponse)
            assertTrue(response.lot != null)

            checkLot(response.lot!!)
        }
    }

    @Test
    fun `READ lot WS`() {

        // given
        val request = LotReadRequest(
            lot = LotReadObject(id = 125),
            debug = debug
        )

        // when
        wsTest<ILotRequest, ILotResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is LotReadResponse)
            assertTrue(response.lot != null)

            checkLot(response.lot!!)
        }
    }

    @Test
    fun `UPDATE lot WS`() {

        // given
        val request = LotUpdateRequest(
            lot = LotUpdateObject(id = 54),
            debug = debug
        )

        // when
        wsTest<ILotRequest, ILotResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is LotUpdateResponse)
            assertTrue(response.lot != null)

            checkLot(response.lot!!)
        }
    }

    @Test
    fun `DELETE lot WS`() {

        // given
        val request = LotDeleteRequest(
            lot = LotDeleteObject(id = 71),
            debug = debug
        )

        // when
        wsTest<ILotRequest, ILotResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is LotDeleteResponse)
            assertTrue(response.lot != null)

            checkLot(response.lot!!)
        }
    }

    @Test
    fun `SEARCH lots WS`() {

        // given
        val request = LotSearchRequest(
            filter = LotSearchFilter(name = "find"),
            debug = debug
        )

        // when
        wsTest<ILotRequest, ILotResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is LotSearchResponse)
            assertTrue(response.lots != null)
            assertEquals(1, response.lots?.size)

            checkLot(response.lots?.first()!!)
        }
    }

    private fun checkLot(refExt: LotResponse) {
        val refInt = StubProcessor.lots.first()

        assertEquals(refInt.id.toLong(), refExt.id)
        assertEquals(refInt.name, refExt.name)
        assertEquals(refInt.description, refExt.description)
        assertEquals(refInt.isCoin, refExt.coin)
        assertEquals(refInt.year.toInt(), refExt.year)
        assertEquals(refInt.catalogueNumber, refExt.catalogueNumber)
        assertEquals(refInt.denomination, refExt.denomination)
        assertEquals(refInt.quantity.toInt(), refExt.quantity)
        assertEquals(refInt.weight, refExt.weight?.mass)
        assertEquals(refInt.materialId.toLong(), refExt.weight?.material?.id)
        assertEquals(refInt.countryId.toLong(), refExt.country?.id)
        assertEquals(Condition.PF, refExt.condition)
        assertEquals(1, refExt.permissions?.size)
        assertTrue(refExt.permissions?.contains(EntityPermission.READ) ?: false)
        assertEquals(2, refExt.photos?.size)
        assertTrue(refExt.photos?.containsAll(setOf(StubProcessor.PHOTO_1, StubProcessor.PHOTO_2)) ?: false)
        assertEquals(refInt.lock.asString(), refExt.lock)
    }

}
