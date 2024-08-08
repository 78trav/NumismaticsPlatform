package ru.numismatics.backend.repo.inmemory.test

import ru.numismatics.backend.common.models.id.toLockId
import ru.numismatics.backend.repo.inmemory.InMemoryRepo
import ru.numismatics.backend.repo.tests.LOCK_ID
import ru.numismatics.backend.repo.tests.UpdateLotBaseRepoTest
import ru.numismatics.backend.stub.StubValues

class UpdateLotInMemoryRepoTest : UpdateLotBaseRepoTest() {

    override val lock = LOCK_ID

    override var lots = StubValues.lots.map { it.copy(lock = lock.toLockId()) }

    override val repo = InMemoryRepo(
        initData = lots,
        randomUuid = { lock }
    )

}