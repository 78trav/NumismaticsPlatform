package ru.numismatics.backend.biz.test.biz

import ru.numismatics.backend.biz.test.validation.runBizTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Country
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.entities.Material
import ru.numismatics.backend.common.models.entities.Section
import ru.numismatics.backend.common.models.id.isEmpty
import ru.numismatics.backend.common.models.id.isNotEmpty
import ru.numismatics.backend.stub.StubProcessor
import kotlin.test.*

class BizValidationCreateTest : BizValidationBase(Command.CREATE) {

    @Test
    fun `CREATE all good`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.first(),
                EntityType.MATERIAL to StubProcessor.materials.first(),
                EntityType.SECTION to StubProcessor.sections.first(),
                EntityType.LOT to StubProcessor.lots.first(),
                EntityType.MARKET_PRICE to StubProcessor.lots.first()
            )
        )
        {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name, entityValidated.name)
            assertEquals(entityRequest.description, entityValidated.description)
            assertTrue(entityValidated.lock.isEmpty())
            when (entityType) {
                EntityType.COUNTRY -> assertTrue { (entityValidated as Country).id.isEmpty() }
                EntityType.MATERIAL -> assertTrue { (entityValidated as Material).id.isEmpty() }
                EntityType.SECTION -> assertTrue { (entityValidated as Section).id.isEmpty() }
                EntityType.LOT -> assertTrue { (entityValidated as Lot).id.isEmpty() }
                EntityType.MARKET_PRICE -> assertTrue { (entityValidated as Lot).id.isNotEmpty() }
                EntityType.UNDEFINED -> assertTrue(false)
            }

        }

    }

    @Test
    fun `CREATE trim name`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.map { it.copy(name = forTrim(it.name)) }.first(),
                EntityType.MATERIAL to StubProcessor.materials.map { it.copy(name = forTrim(it.name)) }.first(),
                EntityType.SECTION to StubProcessor.sections.map { it.copy(name = forTrim(it.name)) }.first(),
                EntityType.LOT to StubProcessor.lots.map { it.copy(name = forTrim(it.name)) }.first(),
                EntityType.MARKET_PRICE to StubProcessor.lots.map { it.copy(name = forTrim(it.name)) }.first()
            )
        )
        {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name.trim(), entityValidated.name)
            assertEquals(entityRequest.description, entityValidated.description)
            assertTrue(entityValidated.lock.isEmpty())
        }

    }

    @Test
    fun `CREATE empty name`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.map { it.copy(name = "") }.first(),
                EntityType.MATERIAL to StubProcessor.materials.map { it.copy(name = "") }.first(),
                EntityType.SECTION to StubProcessor.sections.map { it.copy(name = "") }.first(),
                EntityType.LOT to StubProcessor.lots.map { it.copy(name = "") }.first()
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-name-empty" }
            assertNotNull(er)
        }

    }

    @Test
    fun `CREATE no content name`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.map { it.copy(name = badString) }.first(),
                EntityType.MATERIAL to StubProcessor.materials.map { it.copy(name = badString) }.first(),
                EntityType.SECTION to StubProcessor.sections.map { it.copy(name = badString) }.first(),
                EntityType.LOT to StubProcessor.lots.map { it.copy(name = badString) }.first()
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-name-noContent" }
            assertNotNull(er)
        }

    }

    @Test
    fun `CREATE trim description`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.map { it.copy(description = forTrim(it.description)) }
                    .first(),
                EntityType.MATERIAL to StubProcessor.materials.map { it.copy(description = forTrim(it.description)) }
                    .first(),
                EntityType.SECTION to StubProcessor.sections.map { it.copy(description = forTrim(it.description)) }
                    .first(),
                EntityType.LOT to StubProcessor.lots.map { it.copy(description = forTrim(it.description)) }
                    .first(),
                EntityType.MARKET_PRICE to StubProcessor.lots.map { it.copy(description = forTrim(it.description)) }
                    .first()
            )
        )
        {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name, entityValidated.name)
            assertEquals(entityRequest.description.trim(), entityValidated.description)
            assertTrue(entityValidated.lock.isEmpty())
        }

    }

    @Test
    fun `CREATE empty description`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.map { it.copy(description = "") }.first(),
                EntityType.MATERIAL to StubProcessor.materials.map { it.copy(description = "") }.first(),
                EntityType.SECTION to StubProcessor.sections.map { it.copy(description = "") }.first(),
                EntityType.LOT to StubProcessor.lots.map { it.copy(description = "") }.first()
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-description-empty" }
            assertNotNull(er)
        }

    }

    @Test
    fun `CREATE no content description`() = runBizTest {

        execTest(
            mapOf(
                EntityType.COUNTRY to StubProcessor.countries.map { it.copy(description = badString) }.first(),
                EntityType.MATERIAL to StubProcessor.materials.map { it.copy(description = badString) }.first(),
                EntityType.SECTION to StubProcessor.sections.map { it.copy(description = badString) }.first(),
                EntityType.LOT to StubProcessor.lots.map { it.copy(description = badString) }.first()
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-description-noContent" }
            assertNotNull(er)
        }

    }

    @Test
    fun `CREATE lot wrong name description year`() = runBizTest {

        execTest(
            mapOf(
                EntityType.LOT to StubProcessor.lots.map {
                    it.copy(
                        name = forTrim(""),
                        description = badString,
                        year = 2030u
                    )
                }.first()
            )
        )
        {
            assertEquals(State.FAILING, state)
            assertEquals(3, errors.size)

            listOf(
                "validation-name-empty",
                "validation-description-noContent",
                "validation-year-badValue"
            ).forEach { erCode ->
                val er = errors.firstOrNull { it.code == erCode }
                assertNotNull(er)
            }
        }

    }

}
