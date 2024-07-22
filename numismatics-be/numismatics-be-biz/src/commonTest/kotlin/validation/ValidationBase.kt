package ru.numismatics.backend.biz.test.validation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.EntityType
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
    errorCode: String,
    entities: Map<EntityType, Entity>,
    process: ICorExec<NumismaticsPlatformContext>,
    assertBlock: NumismaticsPlatformContext.(Error?) -> Unit
) {
    entities.forEach { pair ->
        // given
        NumismaticsPlatformContext(
            timeStart = Clock.System.now(),
            entityType = pair.key,
            entityRequest = pair.value
        ).also { context ->
            // when
            process.exec(context)
            // then
            val error = context.errors.find { error ->
                error.code == errorCode
            }
            context.assertBlock(error)
        }
    }
}
