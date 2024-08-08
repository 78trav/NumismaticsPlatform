package ru.numismatics.backend.repo.tests

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.LotId
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.common.repo.base.DbRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class ReadLotBaseRepoTest : LotBaseRepoTest() {

    @Test
    fun readSuccess() = runRepoTest {
        // given
        val expectedLot = lots.first()
        val rq = DbRequest(Command.READ, Lot(id = expectedLot.id))
        // when
        val result = repo.exec(rq)
        // then
        assertIs<DbEntityResponseSuccess<Lot>>(result)

        val actualLot = result.data.firstOrNull()
        assertIs<Lot>(actualLot)

        assertEquals(expectedLot, actualLot)
    }

    @Test
    fun readEmptyIdError() = runRepoTest {
        // given
        val rq = DbRequest(Command.READ, Lot.EMPTY)
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-empty-id")
    }

    @Test
    fun readNotFoundError() = runRepoTest {
        // given
        val rq = DbRequest(Command.READ, Lot(id = LotId(12UL)))
        // when
        val result = repo.exec(rq)
        // then
        checkError(result, "repo-not-found")
    }

}