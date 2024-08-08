package ru.numismatics.platform.app.ktor.test.post.ref
//
//import io.ktor.serialization.kotlinx.*
//import ru.numismatics.backend.api.references.ReferenceTest
//import ru.numismatics.backend.api.refs.models.*
//import ru.numismatics.backend.api.v2.mapper.v2Mapper
//import ru.numismatics.backend.stub.StubProcessor
//import ru.numismatics.platform.app.ktor.test.post.TestApplicationV1
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertTrue
//
//class V1StubReferenceDeleteTest : TestApplicationV1("ref") {
//
//    private val converter = KotlinxWebsocketSerializationConverter(v2Mapper)
//
//    @Test
//    fun `DELETE country WS`() {
//        // given
//        val refInt = StubProcessor.countries.first()
//
//        val request = ReferenceDeleteRequest(
//            referenceType = ReferenceType.COUNTRY,
//            debug = ReferenceTest.debug,
//            id = refInt.id.toLong()
//        )
//
//        // when
//        wsTest<IReferenceRequest, IReferenceResponse>(converter, request) { response ->
//
//            // then
//            assertEquals(ResponseResult.SUCCESS, response.result)
//            assertTrue(response is ReferenceDeleteResponse)
//            assertTrue(response.item != null)
//
//            val refExt = response.item?.reference
//            assertTrue(refExt is Country)
//
//            assertEquals(refInt.id.toLong(), refExt.id)
//            assertEquals(refInt.name, refExt.name)
//            assertEquals(refInt.description, refExt.description)
//        }
//    }
//
//    @Test
//    fun `DELETE material WS`() {
//        // given
//        val refInt = StubProcessor.materials.first()
//
//        val request = ReferenceDeleteRequest(
//            referenceType = ReferenceType.MATERIAL,
//            debug = ReferenceTest.debug,
//            id = refInt.id.toLong()
//        )
//
//        // when
//        wsTest<IReferenceRequest, IReferenceResponse>(converter, request) { response ->
//
//            // then
//            assertEquals(ResponseResult.SUCCESS, response.result)
//            assertTrue(response is ReferenceDeleteResponse)
//            assertTrue(response.item != null)
//
//            val refExt = response.item?.reference
//            assertTrue(refExt is Material)
//
//            assertEquals(refInt.id.toLong(), refExt.id)
//            assertEquals(refInt.name, refExt.name)
//            assertEquals(refInt.description, refExt.description)
//            assertEquals(refInt.probe, refExt.probe)
//        }
//    }
//
//    @Test
//    fun `DELETE section WS`() {
//        // given
//        val refInt = StubProcessor.sections.first()
//
//        val request = ReferenceDeleteRequest(
//            referenceType = ReferenceType.SECTION,
//            debug = ReferenceTest.debug,
//            id = refInt.id.toLong()
//        )
//
//        // when
//        wsTest<IReferenceRequest, IReferenceResponse>(converter, request) { response ->
//
//            // then
//            assertEquals(ResponseResult.SUCCESS, response.result)
//            assertTrue(response is ReferenceDeleteResponse)
//            assertTrue(response.item != null)
//
//            val refExt = response.item?.reference
//            assertTrue(refExt is Section)
//
//            assertEquals(refInt.id.toLong(), refExt.id)
//            assertEquals(refInt.name, refExt.name)
//            assertEquals(refInt.description, refExt.description)
//            assertEquals(refInt.parentId.toLong(), refExt.parentId)
//        }
//    }
//}
