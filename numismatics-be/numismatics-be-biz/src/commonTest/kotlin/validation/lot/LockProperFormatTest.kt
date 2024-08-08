package validations.lot

import ru.numismatics.backend.biz.validation.*
import ru.numismatics.backend.biz.validation.lotClearCommonFieldsJob
import ru.numismatics.backend.biz.validation.lotDeepCopyJob
import ru.numismatics.backend.biz.validation.validationLockProperFormat
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.stub.StubValues
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class LockProperFormatTest {

    companion object {
        private val process = process {
            lotDeepCopyJob()
            lotClearCommonFieldsJob()
            lotClearFieldsForCreateSearchJob()
            validationLockProperFormat<Lot>("")
        }.build()

        private const val ERROR_CODE = "validation-lock-badFormat"
    }

    @Test
    fun `empty lock format`() = runBizTest {

        execTest(
            lot = Lot.EMPTY,
            errorCode = ERROR_CODE,
            process = process
        )
        { error ->
            assertNull(error)
        }
    }

    @Test
    fun `bad lock format`() = runBizTest {

        execTest(
            lot = Lot(LotId(4u), lock = "12!@#$%^&*()_+-=".toLockId()),
            errorCode = ERROR_CODE,
            process = process
        )
        { error ->
            assertNotNull(error)
        }
    }


    @Test
    fun `good lock format`() = runBizTest {

        execTest(
            lot = StubValues.lots.first(),
            errorCode = ERROR_CODE,
            process = process
        )
        { error ->
            assertNull(error)
        }
    }
}