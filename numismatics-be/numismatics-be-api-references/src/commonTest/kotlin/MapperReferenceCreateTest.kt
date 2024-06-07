
import kotlinx.serialization.json.Json
import ru.numismatics.backend.api.references.*
import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.models.core.EntityPermission
import ru.numismatics.backend.common.models.core.stubs.Stubs
import ru.numismatics.backend.common.models.entities.toTransport
import ru.numismatics.backend.common.models.id.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import ru.numismatics.backend.common.models.entities.Material as MaterialInternal
import ru.numismatics.backend.common.models.entities.Country as CountryInternal
import ru.numismatics.backend.common.models.entities.Section as SectionInternal
import ru.numismatics.backend.api.refs.models.EntityPermission as PermissionTransport

class MapperReferenceCreateTest : ReferenceTest(Command.CREATE) {

    @Test
    fun `serialize ReferenceCreateRequest`() {

        // given
        val req = ReferenceCreateRequest(
            debug = debug,
            reference = referencesExternal[ReferenceType.MATERIAL]
        )

        // when
        val json = Json.encodeToString(IReferenceRequest.serializer(), req)

        // then
        println(json)

        assertContains(json, Regex("\"name\":\\s*\"Серебро 925\""))
        assertContains(json, Regex("\"description\":\\s*\"Серебро 925 пробы\""))
        assertContains(json, Regex("\"referenceType\":\\s*\"material\""))
        assertContains(json, Regex("\"probe\":\\s*925.0"))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"success\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun `serialize ReferenceUpdateResponse`() {

        // given
        val req = ReferenceUpdateResponse(
            result = ResponseResult.SUCCESS,
            errors = mutableListOf(error).toTransport(),
            item = ItemResponse(
                reference = (referencesExternal[ReferenceType.MATERIAL] as Material).copy(id = 8),
                permissions = perm.toTransport { it.toTransport() }
            )
        )

        // when
        val json = Json.encodeToString(IReferenceResponse.serializer(), req)

        // then
        println(json)

        assertContains(json, Regex("\"id\":\\s*8"))
        assertContains(json, Regex("\"name\":\\s*\"Серебро 925\""))
        assertContains(json, Regex("\"description\":\\s*\"Серебро 925 пробы\""))
        assertContains(json, Regex("\"referenceType\":\\s*\"material\""))
        assertContains(json, Regex("\"probe\":\\s*925.0"))
        assertContains(json, Regex("\"result\":\\s*\"success\""))
        assertContains(json, Regex("\"responseType\":\\s*\"update\""))
    }

    @Test
    fun `deserialize ReferenceCreateRequest`() {

        // given
        val req = ReferenceCreateRequest(
            debug = debug,
            reference = referencesExternal[ReferenceType.MATERIAL]
        )

        // when
        val json = Json.encodeToString(IReferenceRequest.serializer(), req)
        val obj = Json.decodeFromString<IReferenceRequest>(json) as ReferenceCreateRequest

        // then
        assertEquals(req, obj)
    }

    @Test
    fun `permission internal to transport`() {
        assertEquals(PermissionTransport.CREATE, EntityPermission.CREATE.toTransport())
        assertEquals(PermissionTransport.READ, EntityPermission.READ.toTransport())
        assertEquals(PermissionTransport.UPDATE, EntityPermission.UPDATE.toTransport())
        assertEquals(PermissionTransport.DELETE, EntityPermission.DELETE.toTransport())
    }

    @Test
    fun `set of permission internal to transport`() {

        // given
        val expected = setOf(
            PermissionTransport.READ,
            PermissionTransport.UPDATE,
            PermissionTransport.DELETE
        )

        // when
        val actual = perm
            .toMutableSet()
            .toTransport { it.toTransport() }

        // then
        assertTrue(actual?.containsAll(expected) ?: false)
    }

    @Test
    fun `material from transport`() {

        // given
        val reference = referencesExternal[ReferenceType.MATERIAL] as Material

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
            res.item?.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
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
        val reference = referencesExternal[ReferenceType.COUNTRY] as Country

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
        val res = context.countryCreateToTransport()

        // then
        assertTrue(res.item?.reference is Country)
        assertEquals(ResponseResult.SUCCESS, res.result)

        val referenceOut = res.item?.reference as Country
        assertEquals(referenceIn.id.toLong(), referenceOut.id)
        assertEquals(referenceIn.name, referenceOut.name)
        assertEquals(referenceIn.description, referenceOut.description)

        assertTrue(
            res.item?.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
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
        val reference = referencesExternal[ReferenceType.SECTION] as Section

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
            res.item?.permissions?.containsAll(perm.toMutableSet().toTransport { it.toTransport() }!!) ?: false
        )

        assertEquals(1, res.errors?.size)
        assertEquals(error.code, res.errors?.firstOrNull()?.code)
        assertEquals(error.group, res.errors?.firstOrNull()?.group)
        assertEquals(error.field, res.errors?.firstOrNull()?.field)
        assertEquals(error.message, res.errors?.firstOrNull()?.message)
    }

}
