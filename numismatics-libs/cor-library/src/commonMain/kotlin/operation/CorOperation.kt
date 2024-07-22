package ru.numismatics.platform.libs.cor.operation

import job.ICorJob
import ru.numismatics.platform.libs.cor.core.ICorExec

class CorOperation<T>(
    private val jobs: List<ICorExec<T>>,
    override val name: String,
    override val description: String = "",
    override val blockOn: suspend T.() -> Boolean,
    override val blockExcept: suspend T.(Throwable) -> Unit = {},
) : ICorJob<T> {
    override suspend fun handle(context: T) {
        jobs.forEach {
            it.exec(context)
        }
    }
}
