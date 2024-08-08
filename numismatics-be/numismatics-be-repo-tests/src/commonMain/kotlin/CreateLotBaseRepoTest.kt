package ru.numismatics.backend.repo.tests

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.isEmpty
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.common.repo.base.DbRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

abstract class CreateLotBaseRepoTest : LotBaseRepoTest() {

    @Test
    fun createSuccess() = runRepoTest {
        // given
        val expectedLot = lots.first()
        val rq = DbRequest(Command.CREATE, expectedLot)
        // when
        val result = repo.exec(rq)
        // then
        assertIs<DbEntityResponseSuccess<Lot>>(result)

        val actualLot = result.data.firstOrNull()
        assertIs<Lot>(actualLot)

        assertFalse(actualLot.id.isEmpty())
        assertEquals(expectedLot.name, actualLot.name)
        assertEquals(expectedLot.description, actualLot.description)
        assertEquals(lock, actualLot.lock.asString())
        assertEquals(expectedLot.ownerId, actualLot.ownerId)

        assertEquals(expectedLot.sectionId, actualLot.sectionId)
        assertEquals(expectedLot.isCoin, actualLot.isCoin)
        assertEquals(expectedLot.year, actualLot.year)
        assertEquals(expectedLot.countryId, actualLot.countryId)
        assertEquals(expectedLot.catalogueNumber, actualLot.catalogueNumber)
        assertEquals(expectedLot.denomination, actualLot.denomination)
        assertEquals(expectedLot.materialId, actualLot.materialId)
        assertEquals(expectedLot.weight, actualLot.weight)
        assertEquals(expectedLot.condition, actualLot.condition)
        assertEquals(expectedLot.serialNumber, actualLot.serialNumber)
        assertEquals(expectedLot.quantity, actualLot.quantity)
    }
}