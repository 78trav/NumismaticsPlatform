package validations.lot

import ru.numismatics.backend.biz.validation.*
import ru.numismatics.backend.biz.validation.lotClearCommonFieldsJob
import ru.numismatics.backend.biz.validation.lotDeepCopyJob
import ru.numismatics.backend.biz.validation.validationNameNotEmpty
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.stub.StubValues
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class NameNotEmptyTest {

    companion object {
        private val process = process {
            lotDeepCopyJob()
            lotClearCommonFieldsJob()
            lotClearFieldsForCreateSearchJob()
            validationNameNotEmpty<Lot>("")
        }.build()

        private const val ERROR_CODE = "validation-name-empty"
    }

    @Test
    fun `empty name`() = runBizTest {

        execTest(
            lot = Lot.EMPTY,
            errorCode = ERROR_CODE,
            process = process
        )
        { error ->
            assertNotNull(error)
        }
    }

    @Test
    fun `good name`() = runBizTest {

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