package ru.numismatics.backend.api.v1.test

import ru.numismatics.backend.api.v1.fromTransport
import ru.numismatics.backend.api.v1.models.*
import ru.numismatics.backend.api.v1.toTransport
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.mappers.stubCaseToInternal
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.EntityPermission as EntityPermissionInternal
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.core.Condition as ConditionInternal

class MapperLotTest : TestValues() {

    @Test
    fun `stubCase to internal`() {
        // when
        val stubCase = stubCaseToInternal(debug.stub?.value)

        // then
        assertEquals(Stubs.SUCCESS, stubCase)
    }

    @Test
    fun `lot CREATE request from transport`() {

        // given
        val lotExt = LotCreateObject(
            name = lotInt.name,
            description = lotInt.denomination,
            coin = lotInt.isCoin,
            year = lotInt.year.toInt(),
            catalogueNumber = lotInt.catalogueNumber,
            denomination = lotInt.denomination,
            weight = lotInt.weight,
            condition = Condition.PF,
            quantity = lotInt.quantity.toInt(),
            photos = listOf(PHOTO_1, PHOTO_2),
            countryId = lotInt.countryId.toLong(),
            materialId = lotInt.materialId.toLong()
        )

        val req = LotCreateRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as ILotRequest)

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
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(lotExt.coin, lotInt.isCoin)
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

        assertTrue(lotInt.marketPrice.isEmpty())
    }

    @Test
    fun `lot CREATE response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.CREATE).toTransport()

        // then
        assertTrue(res is LotCreateResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)

        assertEquals(lotInt.permissions.size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ, EntityPermission.UPDATE, EntityPermission.DELETE)
            ) ?: false
        )

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot UPDATE request from transport`() {

        // given
        val lotExt = LotUpdateObject(
            id = 100,
            name = lotInt.name,
            description = lotInt.denomination,
            coin = lotInt.isCoin,
            year = lotInt.year.toInt(),
            catalogueNumber = lotInt.catalogueNumber,
            denomination = lotInt.denomination,
            weight = lotInt.weight,
            condition = Condition.PF,
            quantity = lotInt.quantity.toInt(),
            photos = listOf(PHOTO_1, PHOTO_2),
            countryId = lotInt.countryId.toLong(),
            materialId = lotInt.materialId.toLong(),
            lock = lotInt.lock.asString()
        )

        val req = LotUpdateRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as ILotRequest)

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
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(lotExt.lock, lotInt.lock.asString())
        assertEquals(lotExt.coin, lotInt.isCoin)
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

        assertTrue(lotInt.marketPrice.isEmpty())
    }

    @Test
    fun `lot UPDATE response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.UPDATE).toTransport()

        // then
        assertTrue(res is LotUpdateResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)

        assertEquals(lotInt.permissions.size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ, EntityPermission.UPDATE, EntityPermission.DELETE)
            ) ?: false
        )

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot READ request from transport`() {

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
        context.fromTransport(req as ILotRequest)

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
    fun `lot READ response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.READ).toTransport()

        // then
        assertTrue(res is LotReadResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)

        assertEquals(lotInt.permissions.size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ, EntityPermission.UPDATE, EntityPermission.DELETE)
            ) ?: false
        )

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot DELETE request from transport`() {

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
        context.fromTransport(req as ILotRequest)

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
    fun `lot DELETE response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.DELETE).toTransport()

        // then
        assertTrue(res is LotDeleteResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lot
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)

        assertEquals(lotInt.permissions.size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ, EntityPermission.UPDATE, EntityPermission.DELETE)
            ) ?: false
        )

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `lot SEARCH request from transport`() {

        // given
        val lotExt = LotSearchFilter(
            name = lotInt.name,
            description = lotInt.denomination,
            coin = lotInt.isCoin,
            year = lotInt.year.toInt(),
            denomination = lotInt.denomination,
            condition = Condition.UNC,
            countryId = lotInt.countryId.toLong(),
            materialId = lotInt.materialId.toLong()
        )

        val req = LotSearchRequest(
            debug = debug,
            filter = lotExt
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req as ILotRequest)

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
        assertEquals(SectionId.EMPTY, lotInt.sectionId)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(lotExt.coin, lotInt.isCoin)
        assertEquals(lotExt.year, lotInt.year.toInt())
        assertEquals(lotExt.denomination, lotInt.denomination)
        assertEquals(ConditionInternal.UNC, lotInt.condition)

        assertEquals(lotExt.countryId, lotInt.countryId.toLong())
        assertEquals(lotExt.materialId, lotInt.materialId.toLong())

        assertEquals(0, lotInt.permissions.size)
        assertEquals(0, lotInt.photos.size)
    }

    @Test
    fun `lot SEARCH response to transport`() {

        // given

        // when
        val res = filledContext.copy(
            command = Command.SEARCH,
            entityResponse = mutableListOf(
                lotInt.copy(condition = ConditionInternal.UNC).apply {
                    permissions.add(EntityPermissionInternal.READ)
                }
            )
        ).toTransport()

        // then
        assertTrue(res is LotSearchResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertEquals(1, res.lots?.size)

        val lotExt = res.lots?.get(0)
        assertTrue(lotExt != null)
        assertEquals(lotInt.id.toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.toLong(), lotExt.weight?.material?.id)
        assertEquals(Condition.UNC, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)
        assertEquals(lotInt.photos.size, lotExt.photos?.size)

        lotInt.photos.forEachIndexed { index, s ->
            assertEquals(s.asString(), lotExt.photos?.get(index) ?: "")
        }

        assertEquals(lotInt.countryId.toLong(), lotExt.country?.id)

        assertEquals(1, lotExt.permissions?.size)
        assertTrue(lotExt.permissions?.contains(EntityPermission.READ) ?: false)

        assertEquals(lotInt.lock.asString(), lotExt.lock)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

}
