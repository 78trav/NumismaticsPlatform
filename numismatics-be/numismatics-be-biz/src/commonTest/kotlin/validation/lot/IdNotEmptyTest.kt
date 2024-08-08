package validations.lot

import ru.numismatics.backend.biz.validation.*
import ru.numismatics.backend.biz.validation.lotClearCommonFieldsJob
import ru.numismatics.backend.biz.validation.lotDeepCopyJob
import ru.numismatics.backend.biz.validation.validationIdNotEmpty
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.stub.StubValues
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class IdNotEmptyTest {

    companion object {
        private val process = process {
            lotDeepCopyJob()
            lotClearCommonFieldsJob()
            lotClearFieldsForCreateSearchJob()
            validationIdNotEmpty<Lot>("")
        }.build()

        private const val ERROR_CODE = "validation-id-empty"
    }

    @Test
    fun `empty id`() = runBizTest {

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
    fun `good id`() = runBizTest {

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