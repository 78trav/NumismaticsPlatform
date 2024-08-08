package ru.numismatics.platform.app.ktor.test.post.ref
//
//import io.ktor.client.call.*
//import ru.numismatics.backend.api.references.ReferenceTest
//import ru.numismatics.backend.api.refs.models.*
//import ru.numismatics.backend.stub.StubProcessor
//import ru.numismatics.platform.app.ktor.test.post.TestApplicationV2
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertTrue
//
//class V2StubReferenceDeleteTest : TestApplicationV2("ref") {
//
//    @Test
//    fun `delete country`() {
//        // given
//        val refInt = StubProcessor.countries.first()
//
//        // when
//        postTest(
//            func = "delete",
//            request = ReferenceDeleteRequest(
//                referenceType = ReferenceType.COUNTRY,
//                debug = ReferenceTest.debug,
//                id = refInt.id.toLong()
//            ),
//        ) { response ->
//            // then
//            val responseObj = response.body<ReferenceDeleteResponse>()
//
//            println(responseObj.toString())
//
//            val refExt = response.body<ReferenceDeleteResponse>().item?.reference
//            assertTrue(refExt is Country)
//
//            assertEquals(refInt.id.toLong(), refExt.id)
//            assertEquals(refInt.name, refExt.name)
//            assertEquals(refInt.description, refExt.description)
//        }
//    }
//
//    @Test
//    fun `delete material`() {
//        // given
//        val refInt = StubProcessor.materials.first()
//
//        // when
//        postTest(
//            func = "delete",
//            request = ReferenceDeleteRequest(
//                referenceType = ReferenceType.MATERIAL,
//                debug = ReferenceTest.debug,
//                id = refInt.id.toLong()
//            ),
//        ) { response ->
//            // then
//            val responseObj = response.body<ReferenceDeleteResponse>()
//
//            println(responseObj.toString())
//
//            val refExt = response.body<ReferenceDeleteResponse>().item?.reference
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
//    fun `delete section`() {
//        // given
//        val refInt = StubProcessor.sections.first()
//
//        // when
//        postTest(
//            func = "delete",
//            request = ReferenceDeleteRequest(
//                referenceType = ReferenceType.SECTION,
//                debug = ReferenceTest.debug,
//                id = refInt.id.toLong()
//            ),
//        ) { response ->
//            // then
//            val responseObj = response.body<ReferenceDeleteResponse>()
//
//            println(responseObj.toString())
//
//            val refExt = response.body<ReferenceDeleteResponse>().item?.reference
//            assertTrue(refExt is Section)
//
//            assertEquals(refInt.id.toLong(), refExt.id)
//            assertEquals(refInt.name, refExt.name)
//            assertEquals(refInt.description, refExt.description)
//            assertEquals(refInt.parentId.toLong(), refExt.parentId)
//        }
//    }
//}
