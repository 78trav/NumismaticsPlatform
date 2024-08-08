package validations.lot

import ru.numismatics.backend.biz.validation.*
import ru.numismatics.backend.biz.validation.lotClearCommonFieldsJob
import ru.numismatics.backend.biz.validation.lotDeepCopyJob
import ru.numismatics.backend.biz.validation.validationYear
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.LotId
import ru.numismatics.backend.stub.StubValues
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class YearTest {

    companion object {
        private val process = process {
            lotDeepCopyJob()
            lotClearCommonFieldsJob()
            lotClearFieldsForCreateSearchJob()
            validationYear()
        }.build()

        private const val ERROR_CODE = "validation-year-badValue"
    }

    @Test
    fun `bad year`() = runBizTest {

        execTest(
            lot = Lot(LotId(5u), year = 2028u),
            errorCode = ERROR_CODE,
            process = process
        )
        { error ->
            assertNotNull(error)
        }
    }

    @Test
    fun `good year`() = runBizTest {

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