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
//class V2StubReferenceUpdateTest : TestApplicationV2("ref") {
//
//    @Test
//    fun `update country`() {
//        // given
//        val refInt = StubProcessor.countries.first()
//
//        // when
//        postTest(
//            func = "update",
//            request = ReferenceUpdateRequest(
//                reference = ReferenceTest.country,
//                debug = ReferenceTest.debug
//            ),
//        ) { response ->
//            // then
//            assertEquals(200, response.status.value)
//
//            val refExt = response.body<ReferenceCreateResponse>().item?.reference
//            assertTrue(refExt is Country)
//
//            assertEquals(refInt.id.toLong(), refExt.id)
//            assertEquals(refInt.name, refExt.name)
//            assertEquals(refInt.description, refExt.description)
//        }
//    }
//
//    @Test
//    fun `update material`() {
//        // given
//        val refInt = StubProcessor.materials.first()
//
//        // when
//        postTest(
//            func = "update",
//            request = ReferenceUpdateRequest(
//                reference = ReferenceTest.material,
//                debug = ReferenceTest.debug
//            ),
//        ) { response ->
//            // then
//            assertEquals(200, response.status.value)
//
//            val refExt = response.body<ReferenceCreateResponse>().item?.reference
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
//    fun `update section`() {
//        // given
//        val refInt = StubProcessor.sections.first()
//
//        // when
//        postTest(
//            func = "update",
//            request = ReferenceUpdateRequest(
//                reference = ReferenceTest.section,
//                debug = ReferenceTest.debug
//            ),
//        ) { response ->
//            // then
//            assertEquals(200, response.status.value)
//
//            val refExt = response.body<ReferenceCreateResponse>().item?.reference
//            assertTrue(refExt is Section)
//
//            assertEquals(refInt.id.toLong(), refExt.id)
//            assertEquals(refInt.name, refExt.name)
//            assertEquals(refInt.description, refExt.description)
//            assertEquals(refInt.parentId.toLong(), refExt.parentId)
//        }
//    }
//}
