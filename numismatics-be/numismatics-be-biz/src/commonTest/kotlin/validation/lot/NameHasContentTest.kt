package validations.lot

import ru.numismatics.backend.biz.validation.*
import ru.numismatics.backend.biz.validation.lotClearCommonFieldsJob
import ru.numismatics.backend.biz.validation.lotDeepCopyJob
import ru.numismatics.backend.biz.validation.validationNameHasContent
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.LotId
import ru.numismatics.backend.stub.StubValues
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class NameHasContentTest {

    companion object {
        private val process = process {
            lotDeepCopyJob()
            lotClearCommonFieldsJob()
            lotClearFieldsForCreateSearchJob()
            validationNameHasContent<Lot>("")
        }.build()

        private const val ERROR_CODE = "validation-name-noContent"
    }

    @Test
    fun `empty name`() = runBizTest {

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
    fun `bad name`() = runBizTest {

        execTest(
            lot = Lot(LotId(4u), name = "12!@#$%^&*()_+-="),
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