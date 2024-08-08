package ru.numismatics.backend.repo.inmemory.test

import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.toLockId
import ru.numismatics.backend.repo.inmemory.InMemoryRepo
import ru.numismatics.backend.repo.tests.CreateLotBaseRepoTest
import ru.numismatics.backend.repo.tests.LOCK_ID
import ru.numismatics.backend.stub.StubValues


class CreateLotInMemoryRepoTest : CreateLotBaseRepoTest() {

    override val lock = LOCK_ID

    override var lots = StubValues.lots.map { it.copy(lock = lock.toLockId()) }

    override val repo = InMemoryRepo<Lot>(
        randomUuid = { lock }
    )
}


//class InMemoryRepoTest : LotBaseRepoTest(
//) {
//    override val repo = InMemoryRepo(
//        initData = StubValues.lots,
//        randomUuid = { LOCK_ID }
//    )
//
//    @Test
//    fun `create lot repo test`() {
//        createSuccess()
//    }
//
//    @Test
//    fun `read lot repo test success`() {
//        readSuccess()
//    }
//
//    @Test
//    fun `read lot repo test empty id error`() {
//        readEmptyIdError()
//    }
//
//    @Test
//    fun `read lot repo test not found error`() {
//        readNotFoundError()
//    }
//
//    @Test
//    fun `update lot repo test success`() {
//        updateSuccess()
//    }
//
//    @Test
//    fun `update lot repo test empty id error`() {
//        updateEmptyIdError()
//    }
//
//    @Test
//    fun `update lot repo test not found error`() {
//        updateNotFoundError()
//    }
//
//    @Test
//    fun `update lot repo test empty lock error`() {
//        updateEmptyLockError()
//    }
//
//    @Test
//    fun `update lot repo test concurrency error`() {
//        updateConcurrencyError()
//    }
//
//    @Test
//    fun `update lot repo test empty lock in db error`() {
//        updateEmptyLockInDbError()
//    }
//
//    @Test
//    fun `delete lot repo test success`() {
//        deleteSuccess()
//    }
//
//    @Test
//    fun `delete lot repo test empty id error`() {
//        deleteEmptyIdError()
//    }
//
//    @Test
//    fun `delete lot repo test not found error`() {
//        deleteNotFoundError()
//    }
//
//    @Test
//    fun `delete lot repo test empty lock error`() {
//        deleteEmptyLockError()
//    }
//
//    @Test
//    fun `delete lot repo test concurrency error`() {
//        deleteConcurrencyError()
//    }
//
//    @Test
//    fun `delete lot repo test empty lock in db error`() {
//        deleteEmptyLockInDbError()
//    }
//
//    @Test
//    fun `search lot repo test success 1`() {
//        searchSuccess1()
//    }
//
//    @Test
//    fun `search lot repo test success 2`() {
//        searchSuccess2()
//    }
//
//    @Test
//    fun `search lot repo test success 3`() {
//        searchSuccess3()
//    }
//}