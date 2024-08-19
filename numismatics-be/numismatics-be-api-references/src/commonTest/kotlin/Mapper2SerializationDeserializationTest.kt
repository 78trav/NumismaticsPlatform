package ru.numismatics.backend.api.refs.test

import ru.numismatics.backend.api.references.ReferenceTest
import ru.numismatics.backend.api.references.ReferenceTest.Companion.debug
import ru.numismatics.backend.api.references.ReferenceTest.Companion.material
import ru.numismatics.backend.api.references.toTransport
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class Mapper2SerializationDeserializationTest {

    @Test
    fun `serialize ReferenceCreateRequest`() {

        // given
        val req = ReferenceCreateRequest(
            debug = debug,
            reference = material
        )

        // when
        val source = v2Mapper.encodeToString(IReferenceRequest.serializer(), req)

        // then
        println(source)

        assertContains(source, Regex("\"name\":\\s*\"Серебро 925\""))
        assertContains(source, Regex("\"description\":\\s*\"Серебро 925 пробы\""))
        assertContains(source, Regex("\"referenceType\":\\s*\"material\""))
        assertContains(source, Regex("\"probe\":\\s*925.0"))
        assertContains(source, Regex("\"mode\":\\s*\"stub\""))
        assertContains(source, Regex("\"stub\":\\s*\"success\""))
        assertContains(source, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun `serialize ReferenceUpdateResponse`() {

        // given
        val req = ReferenceUpdateResponse(
            result = ResponseResult.SUCCESS,
            errors = mutableListOf(ReferenceTest.error).toTransport(),
            item = ItemResponse(
                reference = material.copy(id = 8),
                permissions = setOf(EntityPermission.READ)
            )
        )

        // when
        val source = v2Mapper.encodeToString(IReferenceResponse.serializer(), req)

        // then
        println(source)

        assertContains(source, Regex("\"id\":\\s*8"))
        assertContains(source, Regex("\"name\":\\s*\"Серебро 925\""))
        assertContains(source, Regex("\"description\":\\s*\"Серебро 925 пробы\""))
        assertContains(source, Regex("\"referenceType\":\\s*\"material\""))
        assertContains(source, Regex("\"probe\":\\s*925.0"))
        assertContains(source, Regex("\"result\":\\s*\"success\""))
        assertContains(source, Regex("\"responseType\":\\s*\"update\""))
    }

    @Test
    fun `deserialize ReferenceCreateRequest`() {

        // given
        val req = ReferenceCreateRequest(
            debug = debug,
            reference = material
        )

        // when
        val source = v2Mapper.encodeToString(IReferenceRequest.serializer(), req)
        val obj = v2Mapper.decodeFromString<IReferenceRequest>(source) as ReferenceCreateRequest

        // then
        assertEquals(req, obj)
    }

}
