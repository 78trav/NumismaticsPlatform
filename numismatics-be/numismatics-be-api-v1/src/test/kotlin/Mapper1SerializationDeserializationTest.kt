package ru.numismatics.backend.api.v1.test

import ru.numismatics.backend.api.v1.models.ILotResponse
import ru.numismatics.backend.api.v1.models.LotCreateRequest
import ru.numismatics.backend.api.v1.models.LotCreateResponse
import ru.numismatics.backend.api.v1.toTransport
import ru.numismatics.backend.api.v1.v1Mapper
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.stub.StubValues
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class Mapper1SerializationDeserializationTest : TestValues() {

    @Test
    fun `serialization LotResponse test`() {

        // given
        val response = filledContext.copy(command = Command.CREATE).toTransport()

        // when
        val source = v1Mapper.writeValueAsString(response)

        // then
        println(source)

        val lot = filledContext.entityResponse.first()

        assertContains(source, Regex("\"responseType\":\\s*\"create\""))
        assertContains(source, Regex("\"result\":\\s*\"success\""))

        assertContains(source, Regex("\"code\":\\s*\"${StubValues.error.code}\""))
        assertContains(source, Regex("\"group\":\\s*\"${StubValues.error.group}\""))
        assertContains(source, Regex("\"field\":\\s*\"${StubValues.error.field}\""))
        assertContains(source, Regex("\"message\":\\s*\"${StubValues.error.message}\""))
        assertContains(source, Regex("\"id\":\\s*${lot.id.asString()}"))

        assertContains(source, Regex("\"name\":\\s*\"${lot.name}\""))
        assertContains(source, Regex("\"description\":\\s*\"${lot.description}\""))
        assertContains(source, Regex("\"coin\":\\s*${lot.isCoin}"))
        assertContains(source, Regex("\"year\":\\s*${lot.year}"))
        assertContains(source, Regex("\"catalogueNumber\":\\s*\"${lot.catalogueNumber}\""))
        assertContains(source, Regex("\"denomination\":\\s*\"${lot.denomination}\""))
        assertContains(source, Regex("\"mass\":\\s*${lot.weight}"))
        assertContains(source, Regex("\"materialId\":\\s*${lot.materialId.asString()}"))
        assertContains(source, Regex("\"condition\":\\s*\"${lot.condition}\""))

        assertContains(source, Regex("\"quantity\":\\s*${lot.quantity}"))
        assertContains(source, Regex("\"ownerId\":\\s*\"${lot.ownerId.asString()}\""))
        assertContains(source, Regex("\"countryId\":\\s*${lot.countryId.asString()}"))
        assertContains(source, Regex("\"lock\":\\s*\"${lot.lock.asString()}\""))
    }

    @Test
    fun `deserialization LotResponse test`() {

        // given
        val response = filledContext.copy(
            command = Command.CREATE
        ).toTransport() as LotCreateResponse

        val source =
            """{"responseType":"create","result":"success","errors":[{"code":"err","group":"test","field":"test","message":"some testing error"}],
            "lots":[{"id":1,"name":"Киров 650","description":"650-летие основания г. Кирова","coin":true,"year":2024,"catalogueNumber":"5111-0502","denomination":"3 рубля","weight":{"mass":31.1,"materialId":1},
            "condition":"PF","quantity":1,"ownerId":"34da1510-a17b-11e9-728d-00241d9157c0","countryId":2,"lock":"test-lock","permissions":["read"]}]}
        """.trimIndent()

        // when
        val obj = v1Mapper.readValue(source, ILotResponse::class.java)

        // then
        println(obj)

        assertEquals(response, obj)
    }

    @Test
    fun deserializeNaked() {
        val source = """
            {"lot": null}
        """.trimIndent()
        val obj = v1Mapper.readValue(source, LotCreateRequest::class.java)

        assertEquals(null, obj.lot)
    }
}
