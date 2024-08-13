
import kotlinx.datetime.LocalDate
import ru.numismatics.backend.api.v2.fromTransport
import ru.numismatics.backend.api.v2.models.*
import ru.numismatics.backend.api.v2.models.Condition
import ru.numismatics.backend.api.v2.toTransport
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.EntityPermission
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.models.core.stubs.Stubs
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.asString
import ru.numismatics.backend.common.models.entities.toLocalDate
import ru.numismatics.backend.common.models.entities.toTransport
import ru.numismatics.backend.common.models.id.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.core.Condition as ConditionInternal
import ru.numismatics.backend.common.models.entities.MarketPrice as MarketPriceInternal

class MapperLotTestV2 {

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

    private val perm = mutableSetOf(EntityPermission.READ, EntityPermission.UPDATE, EntityPermission.DELETE)

    @Test
    fun `lot create request from transport`() {

        // given
        val lotExt = LotCreateObjectV2(
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = Condition.PF,
            quantity = 1,
            photos = listOf("фото1", "фото2"),
            countryId = 2L,
            materialId = 3L,
            marketPrice = MarketPrice("20240607", 10000f),
            sectionId = 73
        )

        val req = LotCreateRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IRequest)

        // then
        assertEquals(Command.CREATE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.LOT, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals(lotExt.name, context.entityRequest.name)
        assertEquals(lotExt.description, context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(LotId.EMPTY, lotInt.id)
        assertEquals(lotExt.sectionId, lotInt.sectionId.toLong())
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(lotExt.isCoin, lotInt.isCoin)
        assertEquals(lotExt.year, lotInt.year.toInt())
        assertEquals(lotExt.catalogueNumber, lotInt.catalogueNumber)
        assertEquals(lotExt.denomination, lotInt.denomination)
        assertEquals(lotExt.weight, lotInt.weight)
        assertEquals(ConditionInternal.PF, lotInt.condition)
        assertEquals(lotExt.quantity, lotInt.quantity.toInt())
        assertEquals(lotExt.photos?.size, lotInt.photos.size)

        lotExt.photos?.forEachIndexed { index, s ->
            assertEquals(s, lotInt.photos[index].asString())
        }

        assertEquals(lotExt.countryId, lotInt.countryId.toLong())
        assertEquals(lotExt.materialId, lotInt.materialId.toLong())

        assertEquals(1, lotInt.marketPrice.size)

        assertEquals(lotExt.marketPrice?.date.toLocalDate(), lotInt.marketPrice[0].date)
        assertEquals(lotExt.marketPrice?.amount, lotInt.marketPrice[0].amount)
    }

    @Test
    fun `lot create response to transport`() {

        // given
        val lotInt = Lot(
            id = LotId(100UL),
            ownerId = UserId("34da1510-a17b-11e9-728d-00241d9157c0"),
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024U,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = ConditionInternal.PF,
            quantity = 1U,
            photos = mutableListOf(Base64String("фото1"), Base64String("фото2")),
            countryId = CountryId(2U),
            materialId = MaterialId(3U),
            marketPrice = mutableListOf(MarketPriceInternal(LocalDate.parse("2024-06-07"), 10000f)),
            sectionId = SectionId(73U)
        ).apply {
            permissions.addAll(perm)
        }

        val context = NumismaticsPlatformContext(
            command = Command.CREATE,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("832"),
            entityType = EntityType.LOT,
            entityResponse = mutableListOf(lotInt)
        )

        // when
        val res = context.toTransport()

        // then
        assertTrue(res is LotCreateResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.isCoin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.value)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)
        assertEquals(lotInt.sectionId.toLong(), lotExt.section?.id)

        assertEquals(1, lotExt.marketPrice?.size)
        assertEquals(lotInt.marketPrice[0].date.asString(), lotExt.marketPrice?.get(0)?.date)
        assertEquals(lotInt.marketPrice[0].amount, lotExt.marketPrice?.get(0)?.amount)

        assertTrue(
            lotExt.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot update request from transport`() {

        // given
        val lotExt = LotUpdateObjectV2(
            id = 100,
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = Condition.PF,
            quantity = 1,
            photos = listOf("фото1", "фото2"),
            countryId = 2,
            materialId = 3,
            lock = "3458398",
            sectionId = 75
        )

        val req = LotUpdateRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IRequest)

        // then
        assertEquals(Command.UPDATE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.LOT, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals(lotExt.name, context.entityRequest.name)
        assertEquals(lotExt.description, context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(lotExt.id, lotInt.id.toLong())
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(lotExt.lock, lotInt.lock.asString())
        assertEquals(lotExt.isCoin, lotInt.isCoin)
        assertEquals(lotExt.year, lotInt.year.toInt())
        assertEquals(lotExt.catalogueNumber, lotInt.catalogueNumber)
        assertEquals(lotExt.denomination, lotInt.denomination)
        assertEquals(lotExt.weight, lotInt.weight)
        assertEquals(ConditionInternal.PF, lotInt.condition)
        assertEquals(lotExt.quantity, lotInt.quantity.toInt())
        assertEquals(lotExt.photos?.size, lotInt.photos.size)

        lotExt.photos?.forEachIndexed { index, s ->
            assertEquals(s, lotInt.photos[index].asString())
        }

        assertEquals(lotExt.countryId, lotInt.countryId.toLong())
        assertEquals(lotExt.materialId, lotInt.materialId.toLong())
        assertEquals(lotExt.sectionId, lotInt.sectionId.toLong())

        assertEquals(0, lotInt.marketPrice.size)
    }

    @Test
    fun `lot update response to transport`() {

        // given
        val lotInt = Lot(
            id = LotId(100UL),
            ownerId = UserId("34da1510-a17b-11e9-728d-00241d9157c0"),
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024U,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = ConditionInternal.PF,
            quantity = 1U,
            photos = mutableListOf(Base64String("фото1"), Base64String("фото2")),
            countryId = CountryId(2U),
            materialId = MaterialId(3U),
            marketPrice = mutableListOf(MarketPriceInternal(LocalDate.parse("2024-06-07"), 10000f)),
            lock = LockId("5698409"),
            sectionId = SectionId(75U)
        ).apply {
            permissions.addAll(perm)
        }

        val context = NumismaticsPlatformContext(
            command = Command.UPDATE,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("832"),
            entityType = EntityType.LOT,
            entityResponse = mutableListOf(lotInt)
        )

        // when
        val res = context.toTransport()

        // then
        assertTrue(res is LotUpdateResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.isCoin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.value)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)
        assertEquals(lotInt.sectionId.toLong(), lotExt.section?.id)

        assertEquals(1, lotExt.marketPrice?.size)
        assertEquals(lotInt.marketPrice[0].date.asString(), lotExt.marketPrice?.get(0)?.date)
        assertEquals(lotInt.marketPrice[0].amount, lotExt.marketPrice?.get(0)?.amount)

        assertTrue(
            lotExt.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot read request from transport`() {

        // given
        val lotExt = LotReadObject(
            id = 100
        )

        val req = LotReadRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IRequest)

        // then
        assertEquals(Command.READ, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.LOT, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(lotExt.id, lotInt.id.toLong())
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals("", lotInt.lock.asString())
        assertEquals(true, lotInt.isCoin)
        assertEquals(0, lotInt.year.toInt())
        assertEquals("", lotInt.catalogueNumber)
        assertEquals("", lotInt.denomination)
        assertEquals(0f, lotInt.weight)
        assertEquals(ConditionInternal.UNDEFINED, lotInt.condition)
        assertEquals(1, lotInt.quantity.toInt())
        assertEquals(0, lotInt.photos.size)

        assertEquals(CountryId.EMPTY, lotInt.countryId)
        assertEquals(MaterialId.EMPTY, lotInt.materialId)

        assertEquals(0, lotInt.marketPrice.size)
        assertEquals(0, lotInt.permissions.size)
    }

    @Test
    fun `lot read response to transport`() {

        // given
        val lotInt = Lot(
            id = LotId(100UL),
            ownerId = UserId("34da1510-a17b-11e9-728d-00241d9157c0"),
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024U,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = ConditionInternal.PF,
            quantity = 1U,
            photos = mutableListOf(Base64String("фото1"), Base64String("фото2")),
            countryId = CountryId(2U),
            materialId = MaterialId(3U),
            marketPrice = mutableListOf(MarketPriceInternal(LocalDate.parse("2024-06-07"), 10000f)),
            lock = LockId("5698409"),
            sectionId = SectionId(79U)
        ).apply {
            permissions.addAll(perm)
        }

        val context = NumismaticsPlatformContext(
            command = Command.READ,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("832"),
            entityType = EntityType.LOT,
            entityResponse = mutableListOf(lotInt)
        )

        // when
        val res = context.toTransport()

        // then
        assertTrue(res is LotReadResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.isCoin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.value)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)
        assertEquals(lotInt.sectionId.toLong(), lotExt.section?.id)

        assertEquals(1, lotExt.marketPrice?.size)
        assertEquals(lotInt.marketPrice[0].date.asString(), lotExt.marketPrice?.get(0)?.date)
        assertEquals(lotInt.marketPrice[0].amount, lotExt.marketPrice?.get(0)?.amount)

        assertTrue(
            lotExt.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot delete request from transport`() {

        // given
        val lotExt = LotDeleteObject(
            id = 100
        )

        val req = LotDeleteRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IRequest)

        // then
        assertEquals(Command.DELETE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.LOT, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(lotExt.id, lotInt.id.toLong())
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals("", lotInt.lock.asString())
        assertEquals(true, lotInt.isCoin)
        assertEquals(0, lotInt.year.toInt())
        assertEquals("", lotInt.catalogueNumber)
        assertEquals("", lotInt.denomination)
        assertEquals(0f, lotInt.weight)
        assertEquals(ConditionInternal.UNDEFINED, lotInt.condition)
        assertEquals(1, lotInt.quantity.toInt())
        assertEquals(0, lotInt.photos.size)

        assertEquals(CountryId.EMPTY, lotInt.countryId)
        assertEquals(MaterialId.EMPTY, lotInt.materialId)

        assertEquals(0, lotInt.marketPrice.size)
        assertEquals(0, lotInt.permissions.size)
    }

    @Test
    fun `lot delete response to transport`() {

        // given
        val lotInt = Lot(
            id = LotId(100UL),
            ownerId = UserId("34da1510-a17b-11e9-728d-00241d9157c0"),
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024U,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = ConditionInternal.PF,
            quantity = 1U,
            photos = mutableListOf(Base64String("фото1"), Base64String("фото2")),
            countryId = CountryId(2U),
            materialId = MaterialId(3U),
            marketPrice = mutableListOf(MarketPriceInternal(LocalDate.parse("2024-06-07"), 10000f)),
            lock = LockId("5698409"),
            sectionId = SectionId(81U)
        ).apply {
            permissions.addAll(perm)
        }

        val context = NumismaticsPlatformContext(
            command = Command.DELETE,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("832"),
            entityType = EntityType.LOT,
            entityResponse = mutableListOf(lotInt)
        )

        // when
        val res = context.toTransport()

        // then
        assertTrue(res is LotDeleteResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.isCoin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.value)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)
        assertEquals(lotInt.sectionId.toLong(), lotExt.section?.id)

        assertEquals(1, lotExt.marketPrice?.size)
        assertEquals(lotInt.marketPrice[0].date.asString(), lotExt.marketPrice?.get(0)?.date)
        assertEquals(lotInt.marketPrice[0].amount, lotExt.marketPrice?.get(0)?.amount)

        assertTrue(
            lotExt.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot search request from transport`() {

        // given
        val lotExt = LotSearchFilterV2(
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024,
            denomination = "3 рубля",
            condition = Condition.UNC,
            countryId = 2L,
            materialId = 3L,
            sectionId = 83
        )

        val req = LotSearchRequest(
            debug = debug,
            filter = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as IRequest)

        // then
        assertEquals(Command.SEARCH, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.LOT, context.entityType)
        assertTrue(context.entityRequest is Lot)

        assertEquals(lotExt.name, context.entityRequest.name)
        assertEquals(lotExt.description, context.entityRequest.description)

        val lotInt = context.entityRequest as Lot

        assertEquals(LotId.EMPTY, lotInt.id)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(lotExt.isCoin, lotInt.isCoin)
        assertEquals(lotExt.year, lotInt.year.toInt())
        assertEquals(lotExt.denomination, lotInt.denomination)
        assertEquals(ConditionInternal.UNC, lotInt.condition)

        assertEquals(lotExt.countryId, lotInt.countryId.toLong())
        assertEquals(lotExt.materialId, lotInt.materialId.toLong())
        assertEquals(lotExt.sectionId, lotInt.sectionId.toLong())

        assertEquals(0, lotInt.permissions.size)
        assertEquals(0, lotInt.photos.size)
        assertEquals(0, lotInt.marketPrice.size)
    }

    @Test
    fun `lot search response to transport`() {

        // given
        val lotInt = Lot(
            id = LotId(100UL),
            ownerId = UserId("34da1510-a17b-11e9-728d-00241d9157c0"),
            name = "Киров 650",
            description = "650-летие основания г. Кирова",
            isCoin = true,
            year = 2024U,
            catalogueNumber = "5111-0502",
            denomination = "3 рубля",
            weight = 31.1f,
            condition = ConditionInternal.UNC,
            quantity = 1U,
            photos = mutableListOf(Base64String("фото1"), Base64String("фото2")),
            countryId = CountryId(2U),
            materialId = MaterialId(3U),
            marketPrice = mutableListOf(MarketPriceInternal(LocalDate.parse("2024-06-07"), 10000f)),
            lock = LockId("762657263"),
            sectionId = SectionId(83U)
        ).apply {
            permissions.addAll(perm)
        }

        val context = NumismaticsPlatformContext(
            command = Command.SEARCH,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("832"),
            entityType = EntityType.LOT,
            entityResponse = mutableListOf(lotInt)
        )

        // when
        val res = context.toTransport()

        // then
        assertTrue(res is LotSearchResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertEquals(1, res.lots?.size)

        val lotExt = res.lots?.get(0)
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.isCoin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.value)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.UNC, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)
        assertEquals(lotInt.sectionId.toLong(), lotExt.section?.id)

        assertEquals(1, lotExt.marketPrice?.size)
        assertEquals(lotInt.marketPrice[0].date.asString(), lotExt.marketPrice?.get(0)?.date)
        assertEquals(lotInt.marketPrice[0].amount, lotExt.marketPrice?.get(0)?.amount)

        assertTrue(
            lotExt.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

}
