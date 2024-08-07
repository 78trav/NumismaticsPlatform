package ru.numismatics.backend.api.refs.test

import ru.numismatics.backend.api.references.*
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
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

class MapperReferenceDeleteTest: ReferenceTest(Command.DELETE) {

    @Test
    fun `material from transport`() {

        // given
        val req = ReferenceDeleteRequest(
            id = 54,
            debug = debug,
            referenceType = ReferenceType.MATERIAL
        )
        val valueId = req.id.toMaterialId()

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req)

        // then
        assertEquals(command, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.MATERIAL, context.entityType)
        assertTrue(context.entityRequest is MaterialInternal)
        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)
        assertEquals(0f, (context.entityRequest as MaterialInternal).probe)
        assertEquals(valueId, (context.entityRequest as MaterialInternal).id)
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
        val res = context.materialDeleteToTransport()

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
        val req = ReferenceDeleteRequest(
            id = 55,
            debug = debug,
            referenceType = ReferenceType.COUNTRY
        )
        val valueId = req.id.toCountryId()

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req)

        // then
        assertEquals(command, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.COUNTRY, context.entityType)
        assertTrue(context.entityRequest is CountryInternal)
        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)
        assertEquals(valueId, (context.entityRequest as CountryInternal).id)
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
        val res = context.countryDeleteToTransport()

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
        val req = ReferenceDeleteRequest(
            id = 57,
            debug = debug,
            referenceType = ReferenceType.SECTION
        )
        val valueId = req.id.toSectionId()

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req)

        // then
        assertEquals(command, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.SECTION, context.entityType)
        assertTrue(context.entityRequest is SectionInternal)
        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)
        assertEquals(SectionId.EMPTY, (context.entityRequest as SectionInternal).parentId)
        assertEquals(valueId, (context.entityRequest as SectionInternal).id)
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
        val res = context.sectionDeleteToTransport()

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
