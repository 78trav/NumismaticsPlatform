package ru.numismatics.backend.repo.pg.test

import ru.numismatics.backend.common.models.id.toLockId
import ru.numismatics.backend.repo.pg.PgRepoLot
import ru.numismatics.backend.repo.tests.DeleteLotBaseRepoTest
import ru.numismatics.backend.repo.tests.LOCK_ID
import ru.numismatics.backend.stub.StubValues

class DeleteLotRgRepoTest : DeleteLotBaseRepoTest() {

    override val lock = LOCK_ID

    override val repo = PgRepoLot(
        properties = properties,
        randomUuid = { lock }
    )

    override var lots = repo.save(StubValues.lots.map { it.copy(lock = lock.toLockId()) }).toList().also {
        println(it)
    }

}