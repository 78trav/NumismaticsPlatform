package ru.numismatics.backend.api.v2.test

import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.api.v2.models.ILotResponse
import ru.numismatics.backend.api.v2.models.LotCreateRequest
import ru.numismatics.backend.api.v2.models.LotCreateResponse
import ru.numismatics.backend.api.v2.toTransport
import ru.numismatics.backend.api.v2.v2ResponseSerialize
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityPermission
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class Mapper2SerializationDeserializationTest : TestValues() {

    @Test
    fun `serialization LotResponse test`() {

        // given
        val response = filledContext.copy(command = Command.CREATE).toTransport()

        // when
        val source = v2ResponseSerialize(response)

        // then
        println(source)

        assertContains(source, Regex("\"responseType\":\\s*\"create\""))
        assertContains(source, Regex("\"result\":\\s*\"success\""))

        assertContains(source, Regex("\"code\":\\s*\"err\""))
        assertContains(source, Regex("\"group\":\\s*\"request\""))
        assertContains(source, Regex("\"field\":\\s*\"name\""))
        assertContains(source, Regex("\"message\":\\s*\"wrong name\""))
        assertContains(source, Regex("\"id\":\\s*100"))

        assertContains(source, Regex("\"name\":\\s*\"Киров 650\""))
        assertContains(source, Regex("\"description\":\\s*\"650-летие основания г. Кирова\""))
        assertContains(source, Regex("\"coin\":\\s*true"))
        assertContains(source, Regex("\"year\":\\s*2024"))
        assertContains(source, Regex("\"catalogueNumber\":\\s*\"5111-0502\""))
        assertContains(source, Regex("\"denomination\":\\s*\"3 рубля\""))
        assertContains(source, Regex("\"mass\":\\s*31.1"))
        assertContains(source, Regex("\"id\":\\s*3"))
        assertContains(source, Regex("\"condition\":\\s*\"PF\""))

        assertContains(source, Regex("\"quantity\":\\s*1"))
        assertContains(source, Regex("\"ownerId\":\\s*\"34da1510-a17b-11e9-728d-00241d9157c0\""))
        assertContains(source, Regex("\"id\":\\s*2"))
        assertContains(source, Regex("\"lock\":\\s*\"5698409\""))
        assertContains(source, Regex("\"id\":\\s*15"))
    }

    @Test
    fun `deserialization LotResponse test`() {

        // given
        val response = filledContext.copy(
            command = Command.CREATE,
            entityResponse = mutableListOf(
                lotInt.copy().apply {
                    permissions.add(EntityPermission.READ)
                }
            )
        ).toTransport() as LotCreateResponse

        val source = """
            {"responseType":"create","result":"success","errors":[{"code":"err","group":"request","field":"name","message":"wrong name"}],
            "lot":{"id":100,"name":"Киров 650","description":"650-летие основания г. Кирова","coin":true,"year":2024,"catalogueNumber":"5111-0502","denomination":"3 рубля","weight":{"mass":31.1,"material":{"id":3}},"condition":"PF","quantity":1,"photos":["фото1","фото2"],"ownerId":"34da1510-a17b-11e9-728d-00241d9157c0","country":{"id":2},"lock":"5698409","permissions":["read"],"section":{"id":15}}}
        """.trimIndent()

        // when
        val obj = v2Mapper.decodeFromString<ILotResponse>(source)

        // then
        println(obj)

        assertEquals(response, obj)
    }

    @Test
    fun `deserialization LotCreateRequest`() {

//        {"debug":{"mode":"stub","stub":"success"},"lot":{}}
    }

    @Test
    fun deserializeNaked() {
        val source = """
            {"lot": null}
        """.trimIndent()
        val obj = v2Mapper.decodeFromString<LotCreateRequest>(source)

        assertEquals(null, obj.lot)
    }
}
