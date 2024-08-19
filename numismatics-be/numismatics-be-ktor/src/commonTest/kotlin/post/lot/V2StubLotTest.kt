package ru.numismatics.platform.app.ktor.test.post.lot

import io.ktor.client.call.*
import ru.numismatics.backend.api.v2.models.*
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.app.ktor.test.post.TestApplicationV2
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class V2StubLotTest() : TestApplicationV2("v2") {

    private val debug = Debug(
        mode = RequestDebugMode.STUB,
        stub = RequestDebugStubs.SUCCESS
    )

    @Test
    fun `CREATE lot`() {
        // given
        // when
        postTest(
            func = "create",
            request = LotCreateRequest(
                lot = LotCreateObjectV2(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotCreateResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lot
            assertTrue(refExt is LotResponseV2)

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
            assertTrue(refExt is LotResponseV2)

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
                lot = LotUpdateObjectV2(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotUpdateResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lot
            assertTrue(refExt is LotResponseV2)

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
            assertTrue(refExt is LotResponseV2)

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
                filter = LotSearchFilterV2(),
                debug = debug
            ),
        ) { response ->
            // then
            assertEquals(200, response.status.value)

            val responseObj = response.body<LotSearchResponse>()
            println(responseObj.toString())

            val refExt = responseObj.lots?.first()
            assertTrue(refExt is LotResponseV2)

            checkLot(refExt)
        }
    }

    private fun checkLot(refExt: LotResponseV2) {
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
