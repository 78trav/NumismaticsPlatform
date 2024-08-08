package ru.numismatics.backend.api.v2.test

import ru.numismatics.backend.api.v2.fromTransport
import ru.numismatics.backend.api.v2.models.*
import ru.numismatics.backend.api.v2.models.Condition
import ru.numismatics.backend.api.v2.toTransport
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.EntityPermission.READ
import ru.numismatics.backend.common.models.core.EntityPermission as EntityPermissionInternal
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.core.Condition as ConditionInternal

class MapperLotTestV2 : TestValues() {

    private val lotInt = filledContext.entityResponse.first()
    private val error = filledContext.errors.first()

    @Test
    fun `lot CREATE request from transport`() {

        // given
        val lotExt = LotCreateObject2(
            name = lotInt.name,
            description = lotInt.denomination,
            coin = lotInt.isCoin,
            year = lotInt.year.toInt(),
            catalogueNumber = lotInt.catalogueNumber,
            denomination = lotInt.denomination,
            weight = lotInt.weight,
            condition = Condition.PF,
            quantity = lotInt.quantity.toInt(),
            countryId = lotInt.countryId.id().toLong(),
            materialId = lotInt.materialId.id().toLong(),
            sectionId = lotInt.sectionId.id().toLong()
        )

        val req = LotCreateRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext(entityRequest = Lot.EMPTY)

        // when
        context.fromTransport(req as ILotRequest)

        // then
        assertEquals(Command.CREATE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)

        assertEquals(lotExt.name, context.entityRequest.name)
        assertEquals(lotExt.description, context.entityRequest.description)

        val lotInt = context.entityRequest

        assertEquals(LotId.EMPTY, lotInt.id)
        assertEquals(UserId.EMPTY, lotInt.ownerId)
        assertEquals(LockId.NONE, lotInt.lock)
        assertEquals(lotExt.coin, lotInt.isCoin)
        assertEquals(lotExt.year, lotInt.year.toInt())
        assertEquals(lotExt.catalogueNumber, lotInt.catalogueNumber)
        assertEquals(lotExt.denomination, lotInt.denomination)
        assertEquals(lotExt.weight, lotInt.weight)
        assertEquals(ConditionInternal.PF, lotInt.condition)
        assertEquals(lotExt.quantity, lotInt.quantity.toInt())

        assertEquals(lotExt.countryId, lotInt.countryId.id().toLong())
        assertEquals(lotExt.materialId, lotInt.materialId.id().toLong())
        assertEquals(lotExt.sectionId, lotInt.sectionId.id().toLong())
    }

    @Test
    fun `lot CREATE response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.CREATE).toTransport()

        // then
        assertTrue(res is LotCreateResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lots?.firstOrNull()
        assertTrue(lotExt != null)
        assertEquals(lotInt.id().toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.id().toLong(), lotExt.weight?.materialId)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)

        assertEquals(lotInt.countryId.id().toLong(), lotExt.countryId)
        assertEquals(lotInt.sectionId.id().toLong(), lotExt.sectionId)

        assertEquals(lotInt.getPermissions().size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ)
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
        val lotExt = LotUpdateObject2(
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
            countryId = lotInt.countryId.id().toLong(),
            materialId = lotInt.materialId.id().toLong(),
            lock = lotInt.lock.asString(),
            sectionId = lotInt.sectionId.id().toLong()
        )

        val req = LotUpdateRequest(
            debug = debug,
            lot = lotExt
        )

        val context = NumismaticsPlatformContext(entityRequest = Lot.EMPTY)

        // when
        context.fromTransport(req as ILotRequest)

        // then
        assertEquals(Command.UPDATE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)

        assertEquals(lotExt.name, context.entityRequest.name)
        assertEquals(lotExt.description, context.entityRequest.description)

        val lotInt = context.entityRequest

        assertEquals(lotExt.id, lotInt.id().toLong())
        assertTrue(lotInt.ownerId.isEmpty())
        assertEquals(lotExt.lock, lotInt.lock.asString())
        assertEquals(lotExt.coin, lotInt.isCoin)
        assertEquals(lotExt.year, lotInt.year.toInt())
        assertEquals(lotExt.catalogueNumber, lotInt.catalogueNumber)
        assertEquals(lotExt.denomination, lotInt.denomination)
        assertEquals(lotExt.weight, lotInt.weight)
        assertEquals(ConditionInternal.PF, lotInt.condition)
        assertEquals(lotExt.quantity, lotInt.quantity.toInt())

        assertEquals(lotExt.countryId, lotInt.countryId.id().toLong())
        assertEquals(lotExt.materialId, lotInt.materialId.id().toLong())
        assertEquals(lotExt.sectionId, lotInt.sectionId.id().toLong())
    }

    @Test
    fun `lot UPDATE response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.UPDATE).toTransport()

        // then
        assertTrue(res is LotUpdateResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lots?.firstOrNull()
        assertTrue(lotExt != null)
        assertEquals(lotInt.id().toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.id().toLong(), lotExt.weight?.materialId)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)

        assertEquals(lotInt.countryId.id().toLong(), lotExt.countryId)
        assertEquals(lotInt.sectionId.id().toLong(), lotExt.sectionId)

        assertEquals(lotInt.getPermissions().size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ)
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

        val context = NumismaticsPlatformContext(entityRequest = Lot.EMPTY)

        // when
        context.fromTransport(req as ILotRequest)

        // then
        assertEquals(Command.READ, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)

        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)

        val lotInt = context.entityRequest

        assertEquals(lotExt.id, lotInt.id().toLong())
        assertTrue(lotInt.sectionId.isEmpty())
        assertTrue(lotInt.ownerId.isEmpty())
        assertTrue(lotInt.lock.isEmpty())
        assertEquals(true, lotInt.isCoin)
        assertEquals(0, lotInt.year.toInt())
        assertEquals("", lotInt.catalogueNumber)
        assertEquals("", lotInt.denomination)
        assertEquals(0f, lotInt.weight)
        assertEquals(ConditionInternal.UNDEFINED, lotInt.condition)
        assertEquals(1, lotInt.quantity.toInt())

        assertTrue(lotInt.countryId.isEmpty())
        assertTrue(lotInt.materialId.isEmpty())

        assertEquals(0, lotInt.getPermissions().size)
    }

    @Test
    fun `lot READ response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.READ).toTransport()

        // then
        assertTrue(res is LotReadResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lots?.firstOrNull()
        assertTrue(lotExt != null)
        assertEquals(lotInt.id().toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.id().toLong(), lotExt.weight?.materialId)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)

        assertEquals(lotInt.countryId.id().toLong(), lotExt.countryId)
        assertEquals(lotInt.sectionId.id().toLong(), lotExt.sectionId)

        assertEquals(lotInt.getPermissions().size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ)
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

        val context = NumismaticsPlatformContext(entityRequest = Lot.EMPTY)

        // when
        context.fromTransport(req as ILotRequest)

        // then
        assertEquals(Command.DELETE, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)

        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)

        val lotInt = context.entityRequest

        assertEquals(lotExt.id, lotInt.id().toLong())
        assertTrue(lotInt.sectionId.isEmpty())
        assertTrue(lotInt.ownerId.isEmpty())
        assertTrue(lotInt.lock.isEmpty())
        assertEquals(true, lotInt.isCoin)
        assertEquals(0, lotInt.year.toInt())
        assertEquals("", lotInt.catalogueNumber)
        assertEquals("", lotInt.denomination)
        assertEquals(0f, lotInt.weight)
        assertEquals(ConditionInternal.UNDEFINED, lotInt.condition)
        assertEquals(1, lotInt.quantity.toInt())

        assertTrue(lotInt.countryId.isEmpty())
        assertTrue(lotInt.materialId.isEmpty())

        assertEquals(0, lotInt.getPermissions().size)
    }

    @Test
    fun `lot DELETE response to transport`() {

        // given

        // when
        val res = filledContext.copy(command = Command.DELETE).toTransport()

        // then
        assertTrue(res is LotDeleteResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val lotExt = res.lots?.firstOrNull()
        assertTrue(lotExt != null)
        assertEquals(lotInt.id().toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.id().toLong(), lotExt.weight?.materialId)
        assertEquals(Condition.PF, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)

        assertEquals(lotInt.countryId.id().toLong(), lotExt.countryId)
        assertEquals(lotInt.sectionId.id().toLong(), lotExt.sectionId)

        assertEquals(lotInt.getPermissions().size, lotExt.permissions?.size)
        assertTrue(
            lotExt.permissions?.containsAll(
                setOf(EntityPermission.READ)
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
        val lotExt = LotSearchFilter2(
            searchString = "рубля",
            coin = lotInt.isCoin,
            year = lotInt.year.toInt(),
            condition = Condition.UNC,
            countryId = lotInt.countryId.id().toLong(),
            materialId = lotInt.materialId.id().toLong(),
            sectionId = lotInt.sectionId.id().toLong()
        )

        val req = LotSearchRequest(
            debug = debug,
            filter = lotExt
        )

        val context = NumismaticsPlatformContext(entityRequest = Lot.EMPTY)

        // when
        context.fromTransport(req as ILotRequest)

        // then
        assertEquals(Command.SEARCH, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)

        assertEquals(lotExt.searchString, context.entityRequest.description)

        val lotInt = context.entityRequest

        assertTrue(lotInt.id.isEmpty())
        assertTrue(lotInt.ownerId.isEmpty())
        assertTrue(lotInt.lock.isEmpty())
        assertEquals(lotExt.coin, lotInt.isCoin)
        assertEquals(lotExt.year, lotInt.year.toInt())
        assertEquals(ConditionInternal.UNC, lotInt.condition)

        assertEquals(lotExt.countryId, lotInt.countryId.id().toLong())
        assertEquals(lotExt.materialId, lotInt.materialId.id().toLong())
        assertEquals(lotExt.sectionId, lotInt.sectionId.id().toLong())

        assertEquals(0, lotInt.getPermissions().size)
    }

    @Test
    fun `lot SEARCH response to transport`() {

        // given

        // when
        val res = filledContext.copy(
            command = Command.SEARCH,
            entityResponse = mutableListOf(
                lotInt.copy(condition = ConditionInternal.UNC).apply {
                    setPermissions(setOf(READ))
                }
            )
        ).toTransport()

        // then
        assertTrue(res is LotSearchResponse)
        assertEquals(ResponseResult.SUCCESS, res.result)
        assertEquals(1, res.lots?.size)

        val lotExt = res.lots?.get(0)
        assertTrue(lotExt != null)
        assertEquals(lotInt.id().toLong(), lotExt.id)
        assertEquals(lotInt.name, lotExt.name)
        assertEquals(lotInt.description, lotExt.description)
        assertEquals(lotInt.isCoin, lotExt.coin)
        assertEquals(lotInt.year.toInt(), lotExt.year)
        assertEquals(lotInt.catalogueNumber, lotExt.catalogueNumber)
        assertEquals(lotInt.denomination, lotExt.denomination)
        assertEquals(lotInt.weight, lotExt.weight?.mass)
        assertEquals(lotInt.materialId.id().toLong(), lotExt.weight?.materialId)
        assertEquals(Condition.UNC, lotExt.condition)
        assertEquals(lotInt.quantity.toInt(), lotExt.quantity)

        assertEquals(lotInt.countryId.id().toLong(), lotExt.countryId)
        assertEquals(lotInt.sectionId.id().toLong(), lotExt.sectionId)

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