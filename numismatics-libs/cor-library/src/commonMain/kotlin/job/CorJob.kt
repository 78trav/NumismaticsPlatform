package ru.numismatics.platform.libs.cor.work

import job.ICorJob

class CorJob<T>(
    override val name: String,
    override val description: String = "",
    override val blockOn: suspend T.() -> Boolean,
    private val blockHandle: suspend T.() -> Unit = {},
    override val blockExcept: suspend T.(Throwable) -> Unit = {}
) : ICorJob<T> {

    override suspend fun handle(context: T) {
        blockHandle(context)
    }

}
