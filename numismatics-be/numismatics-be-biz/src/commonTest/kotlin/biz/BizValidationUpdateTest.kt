package ru.numismatics.backend.biz.test.biz

import validations.lot.runBizTest
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.id.isNotEmpty
import ru.numismatics.backend.stub.StubValues
import kotlin.test.*

class BizValidationUpdateTest : BizValidationBase(Command.UPDATE) {

    @Test
    fun `UPDATE all good`() = runBizTest {
        execTest(StubValues.lots.first()) {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name, entityValidated.name)
            assertEquals(entityRequest.description, entityValidated.description)
            assertTrue(entityValidated.lock.isNotEmpty())
            assertTrue(entityValidated.id.isNotEmpty())
        }
    }

    @Test
    fun `UPDATE trim name`() = runBizTest {
        execTest(
            StubValues.lots.map { it.copy(name = forTrim(it.name)) }.first()
        ) {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name.trim(), entityValidated.name)
            assertEquals(entityRequest.description, entityValidated.description)
            assertTrue(entityValidated.lock.isNotEmpty())
        }
    }

    @Test
    fun `UPDATE empty name`() = runBizTest {
        execTest(
            StubValues.lots.map { it.copy(name = "") }.first()
        ) {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-name-empty" }
            assertNotNull(er)
        }
    }

    @Test
    fun `UPDATE no content name`() = runBizTest {
        execTest(
            StubValues.lots.map { it.copy(name = badString) }.first()
        ) {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-name-noContent" }
            assertNotNull(er)
        }
    }

    @Test
    fun `UPDATE trim description`() = runBizTest {
        execTest(
            StubValues.lots.map { it.copy(description = forTrim(it.description)) }
                .first()
        ) {
            assertNotEquals(State.FAILING, state)
            assertEquals(0, errors.size)
            assertEquals(entityRequest.name, entityValidated.name)
            assertEquals(entityRequest.description.trim(), entityValidated.description)
            assertTrue(entityValidated.lock.isNotEmpty())
        }
    }

    @Test
    fun `UPDATE empty description`() = runBizTest {
        execTest(
            StubValues.lots.map { it.copy(description = "") }.first()
        ) {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-description-empty" }
            assertNotNull(er)
        }
    }

    @Test
    fun `UPDATE no content description`() = runBizTest {
        execTest(
            StubValues.lots.map { it.copy(description = badString) }.first()
        ) {
            assertEquals(State.FAILING, state)
            assertEquals(1, errors.size)
            val er = errors.firstOrNull { it.code == "validation-description-noContent" }
            assertNotNull(er)
        }
    }

    @Test
    fun `UPDATE lot wrong name description year`() = runBizTest {
        execTest(
            StubValues.lots.map {
                it.copy(
                    name = forTrim(""),
                    description = badString,
                    year = 2030u
                )
            }.first()
        ) {
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