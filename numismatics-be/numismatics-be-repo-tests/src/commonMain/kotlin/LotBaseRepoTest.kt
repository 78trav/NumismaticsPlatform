package ru.numismatics.backend.repo.tests

import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.repo.base.*
import kotlin.test.*

const val LOCK_ID = "test-uuid"

abstract class LotBaseRepoTest {

    abstract val lock: String

    abstract val repo: IRepo<Lot>

    abstract var lots: List<Lot>

    protected fun checkError(result: IDbResponse, errorCode: String) {
        assertIs<DbEntityResponseError<Lot>>(result)
        val er = result.errors.find { it.code == errorCode }
        assertNotNull(er)
    }

}