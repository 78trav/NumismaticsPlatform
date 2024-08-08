package ru.numismatics.backend.repo.tests

import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.CountryId
import ru.numismatics.backend.common.models.id.MaterialId
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.common.repo.base.DbRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

abstract class SearchLotBaseRepoTest : LotBaseRepoTest() {

    @Test
    fun searchSuccess1() = runRepoTest {
        // given
        val rq = DbRequest(Command.SEARCH, Lot(countryId = CountryId(2U)))
        // when
        val result = repo.exec(rq)
        // then
        assertIs<DbEntityResponseSuccess<Lot>>(result)
        assertEquals(2, result.data.size)
        assertTrue(lots.containsAll(result.data))

        println(result)
    }

    @Test
    fun searchSuccess2() = runRepoTest {
        // given
        val rq = DbRequest(
            Command.SEARCH,
            Lot(countryId = CountryId(2U), materialId = MaterialId(1U), condition = Condition.XF_PLUS)
        )
        // when
        val result = repo.exec(rq)
        // then
        assertIs<DbEntityResponseSuccess<Lot>>(result)
        assertEquals(1, result.data.size)
        assertEquals(lots.last(), result.data.first())

        println(result)
    }

    @Test
    fun searchSuccess3() = runRepoTest {
        // given
        val rq = DbRequest(
            Command.SEARCH,
            Lot(countryId = CountryId(2U), materialId = MaterialId(1U), condition = Condition.XF_PLUS, year = 2023U)
        )
        // when
        val result = repo.exec(rq)
        // then
        assertIs<DbEntityResponseSuccess<Lot>>(result)
        assertEquals(0, result.data.size)

        println(result)
    }

}