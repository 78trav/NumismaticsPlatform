package validations.lot

import ru.numismatics.backend.biz.validation.lotClearCommonFieldsJob
import ru.numismatics.backend.biz.validation.lotClearFieldsForCreateSearchJob
import ru.numismatics.backend.biz.validation.lotDeepCopyJob
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.*

class LotStartTest {

    companion object {
        private val process = process {
            lotDeepCopyJob()
            lotClearCommonFieldsJob()
            lotClearFieldsForCreateSearchJob()
        }.build()

        private val lot = Lot(
            id = LotId(1U),
            lock = " wsc".toLockId(),
            name = " test ",
            description = " \n \t test \t \n",
            ownerId = "id ".toUserId(),
            catalogueNumber = " ",
            denomination = " 5  рублей",
            serialNumber = " 764"
        )
    }


    @Test
    fun `deep copy and clear jobs test without command`() = runBizTest {
        // when
        execTest(
            lot = lot,
            errorCode = "",
            process = process
        )
        { error ->
            // then
            assertNull(error)
            assertFalse(entityValidating.isEmpty())

            assertEquals(lot.id, entityValidating.id)
            assertEquals(lot.lock.asString().trim().toLockId(), entityValidating.lock)
            assertEquals(lot.name.trim(), entityValidating.name)
            assertEquals(lot.description.trim(), entityValidating.description)
            assertEquals(lot.ownerId.asString().trim().toUserId(), entityValidating.ownerId)
            assertEquals(lot.catalogueNumber.trim(), entityValidating.catalogueNumber)
            assertEquals(lot.denomination.trim(), entityValidating.denomination)
            assertEquals(lot.serialNumber.trim(), entityValidating.serialNumber)
        }
    }

    @Test
    fun `deep copy and clear jobs test with create command`() = runBizTest {
        // when
        execTest(
            command = Command.CREATE,
            lot = lot,
            errorCode = "",
            process = process
        )
        { error ->
            // then
            assertNull(error)
            assertFalse(entityValidating.isEmpty())

            assertEquals(LotId.EMPTY, entityValidating.id)
            assertEquals(LockId.NONE, entityValidating.lock)
            assertEquals(lot.name.trim(), entityValidating.name)
            assertEquals(lot.description.trim(), entityValidating.description)
            assertEquals(UserId.EMPTY, entityValidating.ownerId)
            assertEquals(lot.catalogueNumber.trim(), entityValidating.catalogueNumber)
            assertEquals(lot.denomination.trim(), entityValidating.denomination)
            assertEquals(lot.serialNumber.trim(), entityValidating.serialNumber)
        }
    }

    @Test
    fun `deep copy and clear jobs test with search command`() = runBizTest {
        // when
        execTest(
            command = Command.SEARCH,
            lot = lot,
            errorCode = "",
            process = process
        )
        { error ->
            // then
            assertNull(error)
            assertFalse(entityValidating.isEmpty())

            assertEquals(LotId.EMPTY, entityValidating.id)
            assertEquals(LockId.NONE, entityValidating.lock)
            assertEquals(lot.name.trim(), entityValidating.name)
            assertEquals(lot.description.trim(), entityValidating.description)
            assertEquals(UserId.EMPTY, entityValidating.ownerId)
            assertEquals(lot.catalogueNumber.trim(), entityValidating.catalogueNumber)
            assertEquals(lot.denomination.trim(), entityValidating.denomination)
            assertEquals(lot.serialNumber.trim(), entityValidating.serialNumber)
        }
    }
}