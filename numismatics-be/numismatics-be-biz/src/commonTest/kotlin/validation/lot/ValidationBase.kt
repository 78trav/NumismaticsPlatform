package validations.lot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.models.entities.*
import ru.numismatics.platform.libs.cor.core.ICorExec

@OptIn(ExperimentalCoroutinesApi::class)
fun runBizTest(block: suspend () -> Unit) = runTest {
    withContext(Dispatchers.Default.limitedParallelism(1)) {
        block()
    }
}

suspend fun execTest(
    command: Command = Command.NONE,
    lot: Lot,
    errorCode: String,
    process: ICorExec<NumismaticsPlatformContext<Lot>>,
    assertBlock: NumismaticsPlatformContext<Lot>.(Error?) -> Unit
) {
    // given
    NumismaticsPlatformContext(
        timeStart = Clock.System.now(),
        command = command,
        entityRequest = lot
    ).also { context ->
        // when
        process.exec(context)
        // then
        val error = errorCode.takeIf { it.isNotEmpty() }.let {
            context.errors.find { error ->
                error.code == it
            }
        }
        context.assertBlock(error)
    }
}