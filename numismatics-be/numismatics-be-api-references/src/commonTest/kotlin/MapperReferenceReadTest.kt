package ru.numismatics.backend.api.refs.test

import kotlinx.serialization.json.Json
import ru.numismatics.backend.api.references.*
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.entities.toTransport
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.stub.StubProcessor
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.entities.Material as MaterialInternal
import ru.numismatics.backend.common.models.entities.Country as CountryInternal
import ru.numismatics.backend.common.models.entities.Section as SectionInternal

class MapperReferenceReadTest : ReferenceTest(Command.READ) {

    @Test
    fun `material self from transport`() {

        // given
        val req = ReferenceReadRequest(
            debug = debug,
            referenceType = ReferenceType.MATERIAL,
            idType = ReadIdType.SELF,
            id = 24
        )
        val valueId = req.id.toMaterialId()

        // when
        val context = NumismaticsPlatformContext()
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
    fun `material parent from transport`() {

        // given
        val req = ReferenceReadRequest(
            debug = debug,
            referenceType = ReferenceType.MATERIAL,
            idType = ReadIdType.PARENT,
            id = 24
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
        assertEquals(MaterialInternal.EMPTY, context.entityRequest)
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
        val res = context.materialReadToTransport()

        // then
        assertEquals(1, res.items?.size)
        assertTrue(res.items?.first()?.reference is Material)

        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.items?.first()?.reference as Material
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)
        assertEquals(referenceIn.probe, referenceOut.probe)

        assertTrue(
            res.items?.first()?.permissions?.containsAll(referenceIn.permissions.toTransport { it.toTransport() }!!)
                ?: false
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
        val req = ReferenceReadRequest(
            debug = debug,
            referenceType = ReferenceType.COUNTRY,
            idType = ReadIdType.SELF,
            id = 25
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
        val res = context.countryReadToTransport()

        // then
        assertEquals(1, res.items?.size)
        assertTrue(res.items?.first()?.reference is Country)

        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.items?.first()?.reference as Country
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)

        assertTrue(
            res.items?.first()?.permissions?.containsAll(referenceIn.permissions.toTransport { it.toTransport() }!!)
                ?: false
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
        val req = ReferenceReadRequest(
            debug = debug,
            referenceType = ReferenceType.SECTION,
            idType = ReadIdType.PARENT,
            id = 26
        )
        val valueParentId = req.id.toSectionId()

        val context = NumismaticsPlatformContext()

        // when
        context.fromTransport(req)

        // then
        assertEquals(command, context.command)
        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(RequestType.STUB, context.requestType)
        assertEquals(EntityType.SECTION, context.entityType)
        assertTrue(context.entityRequest is SectionInternal)
        assertEquals(SectionId.EMPTY, (context.entityRequest as SectionInternal).id)
        assertEquals("", context.entityRequest.name)
        assertEquals("", context.entityRequest.description)
        assertEquals(valueParentId, (context.entityRequest as SectionInternal).parentId)
    }

    @Test
    fun `section to transport`() {

        // given
        val context = NumismaticsPlatformContext(
            command = command,
            state = State.RUNNING,
            errors = mutableListOf(error),
            requestType = RequestType.TEST,
            requestId = RequestId("479"),
            entityType = EntityType.SECTION,
            entityResponse = StubProcessor.sections.toMutableList()
        )

        // when
        val res = context.sectionReadToTransport()

        println(res)

        // then
        assertEquals(StubProcessor.sections.size, res.items?.filter { it.reference is Section }?.size)

        assertEquals(ResponseResult.SUCCESS, res.result)

        res.items?.forEachIndexed { index, item ->
            val referenceIn = StubProcessor.sections[index]
            val referenceOut = item.reference as Section

            assertEquals(referenceIn.id.toLong(), referenceOut.id)
            assertEquals(referenceIn.name, referenceOut.name)
            assertEquals(
                if (referenceIn.description.isEmpty()) null else referenceIn.description,
                referenceOut.description
            )
            assertEquals(
                if (referenceIn.parentId.isEmpty()) null else referenceIn.parentId.toLong(),
                referenceOut.parentId
            )

            assertTrue(
                item.permissions?.containsAll(referenceIn.permissions.toMutableSet().toTransport { it.toTransport() }!!)
                    ?: false
            )
        }

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

}
