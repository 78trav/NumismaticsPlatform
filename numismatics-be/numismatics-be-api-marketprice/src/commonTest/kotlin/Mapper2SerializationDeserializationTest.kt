package ru.numismatics.backend.api.marketprice.test

import ru.numismatics.backend.api.marketprice.models.MarketPriceCreateRequest
import ru.numismatics.backend.api.marketprice.models.MarketPriceResponse
import ru.numismatics.backend.api.marketprice.toTransport
import ru.numismatics.backend.api.marketprice.v2ResponseSerialize
import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.common.models.core.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class Mapper2SerializationDeserializationTest : TestValues() {

    @Test
    fun `serialization MarketPriceResponse test`() {

        // given
        val response = filledContext.copy(command = Command.CREATE).toTransport()

        // when
        val source = v2ResponseSerialize(response)

        // then
        println(source)

        assertContains(source, Regex("\"result\":\\s*\"success\""))

        assertContains(source, Regex("\"code\":\\s*\"err\""))
        assertContains(source, Regex("\"group\":\\s*\"request\""))
        assertContains(source, Regex("\"field\":\\s*\"name\""))
        assertContains(source, Regex("\"message\":\\s*\"wrong name\""))

        assertContains(source, Regex("\"date\":\\s*\"20240407\""))
        assertContains(source, Regex("\"amount\":\\s*4500.0"))

        assertContains(source, Regex("\"date\":\\s*\"20240607\""))
        assertContains(source, Regex("\"amount\":\\s*5000.0"))
    }

    @Test
    fun `deserialization MarketPriceResponse test`() {

        // given
        val response = filledContext.copy(command = Command.CREATE).toTransport()

        val source =
            """{"result":"success","errors":[{"code":"err","group":"request","field":"name","message":"wrong name"}],"marketPrice":[{"date":"20240407","amount":4500.0},{"date":"20240607","amount":5000.0}]}"""

        // when
        val obj = v2Mapper.decodeFromString<MarketPriceResponse>(source)

        // then
        println(obj)

        assertEquals(response, obj)
    }

    @Test
    fun deserializeNaked() {
        val source = """
            {"lot": null}
        """.trimIndent()
        val obj = v2Mapper.decodeFromString<MarketPriceCreateRequest>(source)

        assertEquals(null, obj.lot)
    }
}

