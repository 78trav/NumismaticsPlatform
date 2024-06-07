
import ru.numismatics.backend.api.references.*
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.stubs.Stubs
import ru.numismatics.backend.common.models.entities.toTransport
import ru.numismatics.backend.common.models.id.RequestId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.entities.Material as MaterialInternal
import ru.numismatics.backend.common.models.entities.Country as CountryInternal
import ru.numismatics.backend.common.models.entities.Section as SectionInternal

class MapperReferenceUpdateTest: ReferenceTest(Command.UPDATE) {

    @Test
    fun `material from transport`() {

        // given
        val reference = (referencesExternal[ReferenceType.MATERIAL] as Material).copy(id = 32)

        val req = ReferenceUpdateRequest(
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
        assertEquals(reference.id, (context.entityRequest as MaterialInternal).id.toLong())
    }

    @Test
    fun `material to transport`() {

        // given
        val referenceIn = referencesInternal[EntityType.MATERIAL] as MaterialInternal

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
        val res = context.materialUpdateToTransport()

        // then
        assertTrue(res.item?.reference is Material)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.item?.reference as Material
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)
        assertEquals(referenceIn.probe, referenceOut.probe)

        assertTrue(res.item?.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `country from transport`() {

        // given
        val reference = (referencesExternal[ReferenceType.COUNTRY] as Country).copy(id = 1)

        val req = ReferenceUpdateRequest(
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
        assertEquals(reference.id, (context.entityRequest as CountryInternal).id.toLong())
    }

    @Test
    fun `country to transport`() {

        // given
        val referenceIn = referencesInternal[EntityType.COUNTRY] as CountryInternal

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
        val res = context.countryUpdateToTransport()

        // then
        assertTrue(res.item?.reference is Country)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.item?.reference as Country
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)

        assertTrue(res.item?.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

    @Test
    fun `section from transport`() {

        // given
        val reference = (referencesExternal[ReferenceType.SECTION] as Section).copy(id = 35)

        val req = ReferenceUpdateRequest(
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
        assertEquals(reference.id, (context.entityRequest as SectionInternal).id.toLong())
    }

    @Test
    fun `section to transport`() {

        // given
        val referenceIn = referencesInternal[EntityType.SECTION] as SectionInternal

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
        val res = context.sectionUpdateToTransport()

        // then
        assertTrue(res.item?.reference is Section)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.item?.reference as Section
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)
        assertEquals(referenceIn.parentId.toLong(), referenceOut.parentId)

        assertTrue(res.item?.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false)

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

}
