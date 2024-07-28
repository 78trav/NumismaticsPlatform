package ru.numismatics.backend.api.marketprice.test

import kotlinx.datetime.LocalDate
import ru.numismatics.backend.api.marketprice.fromTransport
import ru.numismatics.backend.api.marketprice.models.*
import ru.numismatics.backend.api.marketprice.toTransport
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.asString
import ru.numismatics.backend.common.models.entities.toLocalDateNP
import ru.numismatics.backend.common.models.id.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal

class MapperMarketPriceTest : TestValues() {

    @Test
    fun `market price CREATE request from transport`() {

        // given
        val mp = MarketPriceCreateObject(
            id = 374,
            marketPrice = MarketPrice("20240607", 5000f)
        )

        val req = MarketPriceCreateRequest(
            debug = debug,
            lot = mp
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IMarketPriceRequest)

        // then
        assertEquals(Command.CREATE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.MARKET_PRICE, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(mp.id, lotInt.id.toLong())
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(true, lotInt.isCoin)
        assertEquals(0, lotInt.year.toInt())
        assertEquals("", lotInt.catalogueNumber)
        assertEquals("", lotInt.denomination)
        assertEquals(0f, lotInt.weight)
        assertEquals(Condition.UNDEFINED, lotInt.condition)
        assertEquals(1, lotInt.quantity.toInt())
        assertEquals(0, lotInt.photos.size)

        assertEquals(CountryId.EMPTY, lotInt.countryId)
        assertEquals(MaterialId.EMPTY, lotInt.materialId)

        assertEquals(1, lotInt.marketPrice.size)

        assertEquals(mp.marketPrice?.date.toLocalDateNP(), lotInt.marketPrice[0].date)
        assertEquals(mp.marketPrice?.amount, lotInt.marketPrice[0].amount)
        assertEquals(0, lotInt.permissions.size)
    }

    @Test
    fun `market price CREATE READ DELETE response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.CREATE).toTransport()

        // then
        assertEquals(ResponseResult.SUCCESS, res.result)

        assertEquals(lotInt.marketPrice.size, res.marketPrice?.size)
        lotInt.marketPrice.forEachIndexed { index, mp ->
            assertEquals(mp.date.asString(), res.marketPrice?.get(index)?.date)
            assertEquals(mp.amount, res.marketPrice?.get(index)?.amount)
        }

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `market price READ request from transport`() {

        // given
        val mp = MarketPriceReadObject(
            id = 374
        )

        val req = MarketPriceReadRequest(
            debug = debug,
            lot = mp
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IMarketPriceRequest)

        // then
        assertEquals(Command.READ, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.MARKET_PRICE, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(mp.id, lotInt.id.toLong())
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(true, lotInt.isCoin)
        assertEquals(0, lotInt.year.toInt())
        assertEquals("", lotInt.catalogueNumber)
        assertEquals("", lotInt.denomination)
        assertEquals(0f, lotInt.weight)
        assertEquals(Condition.UNDEFINED, lotInt.condition)
        assertEquals(1, lotInt.quantity.toInt())
        assertEquals(0, lotInt.photos.size)

        assertEquals(CountryId.EMPTY, lotInt.countryId)
        assertEquals(MaterialId.EMPTY, lotInt.materialId)

        assertEquals(0, lotInt.marketPrice.size)
        assertEquals(0, lotInt.permissions.size)
    }

    @Test
    fun `market price DELETE request from transport`() {

        // given
        val mp = MarketPriceDeleteObject(
            id = 374,
            date = "20240607"
        )

        val req = MarketPriceDeleteRequest(
            debug = debug,
            lot = mp
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IMarketPriceRequest)

        // then
        assertEquals(Command.DELETE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.MARKET_PRICE, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(mp.id, lotInt.id.toLong())
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(true, lotInt.isCoin)
        assertEquals(0, lotInt.year.toInt())
        assertEquals("", lotInt.catalogueNumber)
        assertEquals("", lotInt.denomination)
        assertEquals(0f, lotInt.weight)
        assertEquals(Condition.UNDEFINED, lotInt.condition)
        assertEquals(1, lotInt.quantity.toInt())
        assertEquals(0, lotInt.photos.size)

        assertEquals(CountryId.EMPTY, lotInt.countryId)
        assertEquals(MaterialId.EMPTY, lotInt.materialId)

        assertEquals(1, lotInt.marketPrice.size)

        assertEquals(mp.date.toLocalDateNP(), lotInt.marketPrice[0].date)
        assertEquals(0f, lotInt.marketPrice[0].amount)
        assertEquals(0, lotInt.permissions.size)
    }
}

