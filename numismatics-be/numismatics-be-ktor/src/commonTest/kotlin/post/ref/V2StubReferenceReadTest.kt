package ru.numismatics.platform.app.ktor.test.post.ref

import io.ktor.client.call.*
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.api.references.ReferenceTest
import ru.numismatics.backend.stub.StubProcessor
import ru.numismatics.platform.app.ktor.test.post.TestApplicationV2
import kotlin.test.*

class V2StubReferenceReadTest : TestApplicationV2("ref") {

    @Test
    fun `read countries`() {
        // when
        postTest(
            func = "read",
            request = ReferenceReadRequest(
                referenceType = ReferenceType.COUNTRY,
                idType = ReadIdType.SELF,
                debug = ReferenceTest.debug
            ),
        ) { response ->
            // then
            val responseObj = response.body<ReferenceReadResponse>()

//            println(response.toString())
            println(responseObj.toString())

            assertEquals(200, response.status.value)
            assertFalse(responseObj.items.isNullOrEmpty())
            assertEquals(StubProcessor.countries.size, responseObj.items!!.filter { it.reference is Country }.size)

            StubProcessor.countries.forEachIndexed { index, refInt ->
                val refExt = responseObj.items!![index].reference
                assertTrue(refExt is Country)

                assertEquals(refInt.id.toLong(), refExt.id)
                assertEquals(refInt.name, refExt.name)
                assertEquals(refInt.description, refExt.description)
            }
        }
    }

    @Test
    fun `read materials`() {
        // when
        postTest(
            func = "read",
            request = ReferenceReadRequest(
                referenceType = ReferenceType.MATERIAL,
                idType = ReadIdType.SELF,
                debug = ReferenceTest.debug
            ),
        ) { response ->
            // then
            val responseObj = response.body<ReferenceReadResponse>()

            println(responseObj.toString())

            assertEquals(200, response.status.value)
            assertFalse(responseObj.items.isNullOrEmpty())
            assertEquals(StubProcessor.countries.size, responseObj.items!!.filter { it.reference is Material }.size)

            StubProcessor.materials.forEachIndexed { index, refInt ->
                val refExt = responseObj.items!![index].reference
                assertTrue(refExt is Material)

                assertEquals(refInt.id.toLong(), refExt.id)
                assertEquals(refInt.name, refExt.name)
                assertEquals(refInt.description, refExt.description)
                assertEquals(refInt.probe, refExt.probe)
            }
        }
    }

    @Test
    fun `read section`() {
        // given
        val refInt = StubProcessor.sections.first()

        // when
        postTest(
            func = "read",
            request = ReferenceReadRequest(
                referenceType = ReferenceType.SECTION,
                idType = ReadIdType.SELF,
                id = 1,
                debug = ReferenceTest.debug
            ),
        ) { response ->
            // then
            val responseObj = response.body<ReferenceReadResponse>()

            println(responseObj.toString())

            assertEquals(200, response.status.value)
            assertFalse(responseObj.items.isNullOrEmpty())
            assertEquals(1, responseObj.items!!.size)

            val refExt = responseObj.items!!.first().reference
            assertTrue(refExt is Section)

            assertEquals(refInt.id.toLong(), refExt.id)
            assertEquals(refInt.name, refExt.name)
            assertEquals(refInt.description, refExt.description)
            assertEquals(refInt.parentId.toLong(), refExt.parentId)
        }
    }
}