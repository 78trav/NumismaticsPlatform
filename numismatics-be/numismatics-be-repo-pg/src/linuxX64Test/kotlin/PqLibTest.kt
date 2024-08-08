//package ru.numismatics.backend.repo.pg.test
//
//import ru.numismatics.backend.common.models.core.Command
//import ru.numismatics.backend.common.models.entities.Lot
//import ru.numismatics.backend.common.models.id.isEmpty
//import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
//import ru.numismatics.backend.common.repo.base.DbRequest
//import ru.numismatics.backend.repo.pg.PgProperties
//import ru.numismatics.backend.repo.pg.PgRepoLot
//import ru.numismatics.backend.stub.StubValues
//import kotlin.test.*
//
//class PqLibTest {
//
//    private val lock = "test-uuid"
//
//    private val repo = PgRepoLot(
//        properties =PgProperties(
//            host = "localhost",
//            port = getEnv("pgPort")?.toIntOrNull() ?: 5432,
//            user = "postgres",
//            password = "pg2k24",
//            database = "numismatics",
//            schema = "public",
//            table = "lots"
//        ),
//        randomUuid = { lock }
//    )
//
//    private val lots: MutableList<Lot> = mutableListOf()
//
//    @BeforeTest
//    fun before() = runRepoTest {
//        repo.clear()
//    }
//
//    @Test
//    fun test() = runRepoTest {
//
//        // given
//        val expectedLot = StubValues.lots.first()
//        val rq = DbRequest(Command.CREATE, expectedLot)
//
//        // when
//        val result = repo.exec(rq)
//
//        println(result)
//
//        // then
//        assertIs<DbEntityResponseSuccess<Lot>>(result)
//
//        val actualLot = result.data.firstOrNull()
//        assertIs<Lot>(actualLot)
//
//        assertFalse(actualLot.id.isEmpty())
//        assertEquals(expectedLot.name, actualLot.name)
//        assertEquals(expectedLot.description, actualLot.description)
//        assertEquals(lock, actualLot.lock.asString())
//        assertEquals(expectedLot.ownerId, actualLot.ownerId)
//
//        assertEquals(expectedLot.sectionId, actualLot.sectionId)
//        assertEquals(expectedLot.isCoin, actualLot.isCoin)
//        assertEquals(expectedLot.year, actualLot.year)
//        assertEquals(expectedLot.countryId, actualLot.countryId)
//        assertEquals(expectedLot.catalogueNumber, actualLot.catalogueNumber)
//        assertEquals(expectedLot.denomination, actualLot.denomination)
//        assertEquals(expectedLot.materialId, actualLot.materialId)
//        assertEquals(expectedLot.weight, actualLot.weight)
//        assertEquals(expectedLot.condition, actualLot.condition)
//        assertEquals(expectedLot.serialNumber, actualLot.serialNumber)
//        assertEquals(expectedLot.quantity, actualLot.quantity)
//
//    }
//
//}