package ru.numismatics.backend.repo.tests

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.LockId
import ru.numismatics.backend.common.models.id.LotId
import ru.numismatics.backend.common.models.id.toLockId
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.common.repo.base.DbRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class DeleteLotBaseRepoTest : LotBaseRepoTest() {

    @Test
    fun deleteSuccess() = runRepoTest {
        // given
        val expectedLot = lots.first()
        val rq = DbRequest(Command.DELETE, Lot(id = expectedLot.id, lock = expectedLot.lock))
        // when
        val result = repo.exec(rq)
        // then
        assertIs<DbEntityResponseSuccess<Lot>>(result)

        val actualLot = result.data.firstOrNull()
        assertIs<Lot>(actualLot)

        assertEquals(expectedLot, actualLot)
    }

    @Test
    fun deleteEmptyIdError() = runRepoTest {
        // given
        val rq = DbRequest(Command.DELETE, Lot.EMPTY)
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-empty-id")
    }

    @Test
    fun deleteNotFoundError() = runRepoTest {
        // given
        val rq = DbRequest(Command.DELETE, Lot(id = LotId(125UL), lock = lock.toLockId()))
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-not-found")
    }

    @Test
    fun deleteEmptyLockError() = runRepoTest {
        // given
        val expectedLot = lots.first()
        val rq = DbRequest(Command.DELETE, Lot(id = expectedLot.id, lock = LockId.NONE))
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-lock-empty")
    }

    @Test
    fun deleteConcurrencyError() = runRepoTest {
        // given
        val expectedLot = lots.last()
        val rq = DbRequest(Command.DELETE, Lot(id = expectedLot.id, lock = ("$lock-bad").toLockId()))
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-concurrency")
    }

//    @Test
//    fun deleteEmptyLockInDbError() = runRepoTest {
//        // given
//        val expectedLot = lots.last()
//        val rq = DbRequest(Command.DELETE, Lot(id = expectedLot.id, lock = LOCK_ID.toLockId()))
//        // when
//        val result = repo.exec(rq)
//        // then
//        checkError(result, "system-db-error")
//    }

}