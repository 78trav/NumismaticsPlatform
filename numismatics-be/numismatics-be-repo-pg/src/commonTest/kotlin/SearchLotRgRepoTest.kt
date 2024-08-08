package ru.numismatics.backend.repo.pg.test

import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.toLockId
import ru.numismatics.backend.repo.pg.PgRepoLot
import ru.numismatics.backend.repo.tests.LOCK_ID
import ru.numismatics.backend.repo.tests.SearchLotBaseRepoTest
import ru.numismatics.backend.stub.StubValues
import kotlin.test.BeforeTest

class SearchLotRgRepoTest : SearchLotBaseRepoTest() {

    override val lock = LOCK_ID

    override val repo = PgRepoLot(
        properties = properties,
        randomUuid = { lock }
    )

    override var lots: List<Lot> = emptyList()

    @BeforeTest
    fun before() {
        repo.clear()
        lots = repo.save(StubValues.lots.map { it.copy(lock = lock.toLockId()) }).toList().also {
            println(it)
        }
    }

}