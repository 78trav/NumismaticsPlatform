package ru.numismatics.backend.api.refs.test

import ru.numismatics.backend.api.references.*
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.EntityPermission as EntityPermissionInternal
import ru.numismatics.backend.common.models.entities.toTransport
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.stub.StubProcessor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.entities.Material as MaterialInternal
import ru.numismatics.backend.common.models.entities.Country as CountryInternal
import ru.numismatics.backend.common.models.entities.Section as SectionInternal

class MapperReferenceCreateTest : ReferenceTest(Command.CREATE) {

    @Test
    fun `permission internal to transport`() {
        assertEquals(EntityPermission.CREATE, EntityPermissionInternal.CREATE.toTransport())
        assertEquals(EntityPermission.READ, EntityPermissionInternal.READ.toTransport())
        assertEquals(EntityPermission.UPDATE, EntityPermissionInternal.UPDATE.toTransport())
        assertEquals(EntityPermission.DELETE, EntityPermissionInternal.DELETE.toTransport())
    }

    @Test
    fun `set of permission internal to transport`() {

        // given
        val expected = setOf(
            EntityPermission.READ,
            EntityPermission.UPDATE,
            EntityPermission.DELETE
        )

        // when
        val actual = mutableSetOf(
            EntityPermissionInternal.READ,
            EntityPermissionInternal.UPDATE,
            EntityPermissionInternal.DELETE
        ).toTransport { it.toTransport() }

        // then
        assertTrue(actual?.containsAll(expected) ?: false)
    }

    @Test
    fun `material from transport`() {

        // given
        val reference = material

        val req = ReferenceCreateRequest(
            debug = debug,
            reference = reference
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req)

        // then
        assertEquals(command, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.MATERIAL, context.entityType)
        assertTrue(context.entityRequest is MaterialInternal)
        assertEquals(reference.name, context.entityRequest.name)
        assertEquals(reference.description, context.entityRequest.description)
        assertEquals(reference.probe, (context.entityRequest as MaterialInternal).probe)
        assertEquals(MaterialId.EMPTY, (context.entityRequest as MaterialInternal).id)
    }

    @Test
    fun `material to transport`() {

        // given
        val referenceIn = StubProcessor.materials.first()

        val context = NumismaticsPlatformContext(
            command = command,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("471"),
            entityType = EntityType.MATERIAL,
            entityResponse = mutableListOf(referenceIn)
        )

        // when
        val res = context.materialCreateToTransport()

        // then
        assertTrue(res.item?.reference is Material)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.item?.reference as Material
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)
        assertEquals(referenceIn.probe, referenceOut.probe)

        assertTrue(
            res.item?.permissions?.containsAll(referenceIn.permissions.toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `country from transport`() {

        // given
        val reference = country

        val req = ReferenceCreateRequest(
            debug = debug,
            reference = reference
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req)

        // then
        assertEquals(command, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.COUNTRY, context.entityType)
        assertTrue(context.entityRequest is CountryInternal)
        assertEquals(reference.name, context.entityRequest.name)
        assertEquals(reference.description, context.entityRequest.description)
        assertEquals(CountryId.EMPTY, (context.entityRequest as CountryInternal).id)
    }

    @Test
    fun `country to transport`() {

        // given
        val referenceIn = StubProcessor.countries.first()

        val context = NumismaticsPlatformContext(
            command = command,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("471"),
            entityType = EntityType.COUNTRY,
            entityResponse = mutableListOf(referenceIn)
        )

        // when
        val res = context.countryCreateToTransport()

        // then
        assertTrue(res.item?.reference is Country)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.item?.reference as Country
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)

        assertTrue(
            res.item?.permissions?.containsAll(referenceIn.permissions.toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `section from transport`() {

        // given
        val reference = section

        val req = ReferenceCreateRequest(
            debug = debug,
            reference = reference
        )

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req)

        // then
        assertEquals(command, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.SECTION, context.entityType)
        assertTrue(context.entityRequest is SectionInternal)
        assertEquals(reference.name, context.entityRequest.name)
        assertEquals(reference.description, context.entityRequest.description)
        assertEquals(reference.parentId, (context.entityRequest as SectionInternal).parentId.toLong())
        assertEquals(SectionId.EMPTY, (context.entityRequest as SectionInternal).id)
    }

    @Test
    fun `section to transport`() {

        // given
        val referenceIn = StubProcessor.sections.first()

        val context = NumismaticsPlatformContext(
            command = command,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("471"),
            entityType = EntityType.SECTION,
            entityResponse = mutableListOf(referenceIn)
        )

        // when
        val res = context.sectionCreateToTransport()

        // then
        assertTrue(res.item?.reference is Section)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.item?.reference as Section
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)
        assertEquals(referenceIn.parentId.toLong(), referenceOut.parentId)

        assertTrue(
            res.item?.permissions?.containsAll(referenceIn.permissions.toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }
}
