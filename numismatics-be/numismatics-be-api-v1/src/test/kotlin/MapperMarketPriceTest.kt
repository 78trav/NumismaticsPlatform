
import kotlinx.datetime.LocalDate
import ru.numismatics.backend.api.v1.fromTransport
import ru.numismatics.backend.api.v1.models.*
import ru.numismatics.backend.api.v1.toTransport
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.models.core.stubs.Stubs
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.asString
import ru.numismatics.backend.common.models.entities.toLocalDate
import ru.numismatics.backend.common.models.id.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal

class MapperMarketPriceTest {

    private val debug = Debug(
        mode = RequestDebugMode.STUB,
        stub = RequestDebugStubs.SUCCESS
    )

    private val error = Error(
        code = "err",
        group = "request",
        field = "name",
        message = "wrong name"
    )

    @Test
    fun `market price create request from transport`() {

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
        context.fromTransport(req as IRequest)

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

        assertEquals(mp.marketPrice?.date.toLocalDate(), lotInt.marketPrice[0].date)
        assertEquals(mp.marketPrice?.amount, lotInt.marketPrice[0].amount)
        assertEquals(0, lotInt.permissions.size)
    }

    @Test
    fun `market price create, read, delete response to transport`() {

        // given
        val lotInt = Lot(
            id = LotId(374U),
            marketPrice = mutableListOf(
                MarketPriceInternal(
                    LocalDate.parse("2024-04-07"),
                    4500f
                ),
                MarketPriceInternal(
                    LocalDate.parse("2024-06-07"),
                    5000f
                )
            )
        )

        val context = NumismaticsPlatformContext(
            command = Command.CREATE,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("5765"),
            entityType = EntityType.MARKET_PRICE,
            entityResponse = mutableListOf(lotInt)
        )

        // when
        val res = context.toTransport()

        // then
        assertTrue(res is MarketPriceResponse)
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
    fun `market price read request from transport`() {

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
        context.fromTransport(req as IRequest)

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
    fun `market price delete request from transport`() {

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
        context.fromTransport(req as IRequest)

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

        assertEquals(mp.date.toLocalDate(), lotInt.marketPrice[0].date)
        assertEquals(0f, lotInt.marketPrice[0].amount)
        assertEquals(0, lotInt.permissions.size)
    }
}
