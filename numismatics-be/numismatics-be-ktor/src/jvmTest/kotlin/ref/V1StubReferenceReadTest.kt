package ru.numismatics.platform.app.ktor.test.post.ref

import io.ktor.serialization.kotlinx.*
import ru.numismatics.backend.api.references.ReferenceTest
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.app.ktor.test.post.TestApplicationV1
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class V1StubReferenceReadTest : TestApplicationV1("ref") {

    private val converter = KotlinxWebsocketSerializationConverter(v2Mapper)

    @Test
    fun `READ country WS`() {
        // given
        val request = ReferenceReadRequest(
            referenceType = ReferenceType.COUNTRY,
            idType = ReadIdType.SELF,
            debug = ReferenceTest.debug
        )

        // when
        wsTest<IReferenceRequest, IReferenceResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is ReferenceReadResponse)
            assertFalse(response.items.isNullOrEmpty())
            assertEquals(StubProcessor.countries.size, response.items!!.filter { it.reference is Country }.size)

            StubProcessor.countries.forEachIndexed { index, refInt ->
                val refExt = response.items!![index].reference
                assertTrue(refExt is Country)

                assertEquals(refInt.id.toLong(), refExt.id)
                assertEquals(refInt.name, refExt.name)
                assertEquals(refInt.description, refExt.description)
            }
        }
    }

    @Test
    fun `READ material WS`() {
        // given
        val request = ReferenceReadRequest(
            referenceType = ReferenceType.MATERIAL,
            idType = ReadIdType.SELF,
            debug = ReferenceTest.debug
        )

        // when
        wsTest<IReferenceRequest, IReferenceResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is ReferenceReadResponse)

            assertFalse(response.items.isNullOrEmpty())
            assertEquals(StubProcessor.countries.size, response.items!!.filter { it.reference is Material }.size)

            StubProcessor.materials.forEachIndexed { index, refInt ->
                val refExt = response.items!![index].reference
                assertTrue(refExt is Material)

                assertEquals(refInt.id.toLong(), refExt.id)
                assertEquals(refInt.name, refExt.name)
                assertEquals(refInt.description, refExt.description)
                assertEquals(refInt.probe, refExt.probe)
            }
        }
    }

    @Test
    fun `READ section WS`() {
        // given
        val refInt = StubProcessor.sections.first()

        val request = ReferenceReadRequest(
            referenceType = ReferenceType.SECTION,
            idType = ReadIdType.SELF,
            id = 1,
            debug = ReferenceTest.debug
        )

        // when
        wsTest<IReferenceRequest, IReferenceResponse>(converter, request) { response ->

            // then
            assertEquals(ResponseResult.SUCCESS, response.result)
            assertTrue(response is ReferenceReadResponse)

            assertFalse(response.items.isNullOrEmpty())
            assertEquals(1, response.items!!.size)

            val refExt = response.items!!.first().reference
            assertTrue(refExt is Section)

            assertEquals(refInt.id.toLong(), refExt.id)
            assertEquals(refInt.name, refExt.name)
            assertEquals(refInt.description, refExt.description)
            assertEquals(refInt.parentId.toLong(), refExt.parentId)

        }
    }
}