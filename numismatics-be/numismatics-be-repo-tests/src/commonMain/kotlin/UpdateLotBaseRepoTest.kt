package ru.numismatics.backend.repo.tests

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.LotId
import ru.numismatics.backend.common.models.id.SectionId
import ru.numismatics.backend.common.models.id.toLockId
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.common.repo.base.DbRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class UpdateLotBaseRepoTest : LotBaseRepoTest() {

    @Test
    fun updateSuccess() = runRepoTest {
        // given
        val expectedLot = lots.first().copy(name = "Киров 650 лет", sectionId = SectionId(7U))
        val rq = DbRequest(Command.UPDATE, expectedLot)
        // when
        val result = repo.exec(rq)
        // then
        assertIs<DbEntityResponseSuccess<Lot>>(result)

        val actualLot = result.data.firstOrNull()
        assertIs<Lot>(actualLot)

        assertEquals(expectedLot.id, actualLot.id)
        assertEquals(expectedLot.name, actualLot.name)
        assertEquals(expectedLot.description, actualLot.description)
        assertEquals(LOCK_ID, actualLot.lock.asString())
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

    @Test
    fun updateEmptyIdError() = runRepoTest {
        // given
        val expectedLot =
            lots.first().copy(id = LotId.EMPTY, name = "Киров 650 лет", sectionId = SectionId(7U))
        val rq = DbRequest(Command.UPDATE, expectedLot)
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-empty-id")
    }

    @Test
    fun updateNotFoundError() = runRepoTest {
        // given
        val expectedLot =
            lots.first().copy(id = LotId(123UL), name = "Киров 650 лет", sectionId = SectionId(7U))
        val rq = DbRequest(Command.UPDATE, expectedLot)
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-not-found")
    }

    @Test
    fun updateEmptyLockError() = runRepoTest {
        // given
        val expectedLot = lots.first().copy(lock = LockId.NONE)
        val rq = DbRequest(Command.UPDATE, expectedLot)
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-lock-empty")
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        // given
        val expectedLot = lots.first().copy(lock = ("$lock-bad").toLockId())
        val rq = DbRequest(Command.UPDATE, expectedLot)
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-concurrency")
    }

//    @Test
//    fun updateEmptyLockInDbError() = runRepoTest {
//        // given
//        val expectedLot = lots.first().copy(lock = lock.toLockId())
//        val rq = DbRequest(Command.UPDATE, expectedLot)
//        // when
//        val result = repo.exec(rq)
//        // then
//        checkError(result, "system-db-error")
//    }

}