package ru.numismatics.backend.common.repo.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.numismatics.backend.common.helpers.errorSystem
import ru.numismatics.backend.common.models.entities.Entity
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class RepoBase<T : Entity> : IRepo<T> {

    protected suspend fun tryMethod(
        timeout: Duration = 10.seconds,
        coroutineContext: CoroutineContext = Dispatchers.IO,
        block: suspend () -> IDbResponse
    ) = try {
        withTimeout(timeout) {
            withContext(coroutineContext) {
                block()
            }
        }
    } catch (e: Throwable) {
        DbEntityResponseError<T>(errors = listOf(errorSystem("methodException", e)))
    }
}