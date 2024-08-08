package validations.lot

import ru.numismatics.backend.biz.validation.*
import ru.numismatics.backend.biz.validation.lotClearCommonFieldsJob
import ru.numismatics.backend.biz.validation.lotDeepCopyJob
import ru.numismatics.backend.biz.validation.validationDescriptionNotEmpty
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.models.id.LotId
import ru.numismatics.platform.libs.cor.operation.process
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DescriptionNotEmptyTest {

    companion object {
        private val process = process<NumismaticsPlatformContext<Lot>> {
            lotDeepCopyJob()
            lotClearCommonFieldsJob()
            lotClearFieldsForCreateSearchJob()
            validationDescriptionNotEmpty("")
        }.build()

        private const val ERROR_CODE = "validation-description-empty"
    }

    @Test
    fun `empty description`() = runBizTest {

        execTest(
            lot = Lot(LotId(6u), description = " \n \t  \t \n"),
            errorCode = ERROR_CODE,
            process = process
        )
        { error ->
            assertNotNull(error)
        }
    }

    @Test
    fun `good description`() = runBizTest {

        execTest(
            lot = Lot(LotId(6u), description = "\n\t test\t\n"),
            errorCode = ERROR_CODE,
            process = process
        )
        { error ->
            assertNull(error)
        }
    }
}