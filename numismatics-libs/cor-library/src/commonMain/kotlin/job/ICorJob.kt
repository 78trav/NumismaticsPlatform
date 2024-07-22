package job

import ru.numismatics.platform.libs.cor.core.ICorExec

interface ICorJob<T> : ICorExec<T> {

    override val name: String
    override val description: String

    val blockOn: suspend T.() -> Boolean
    val blockExcept: suspend T.(Throwable) -> Unit

    suspend fun handle(context: T)

    private suspend fun on(context: T): Boolean = context.blockOn()

    private suspend fun except(context: T, e: Throwable) {
        context.blockExcept(e)
    }

    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                except(context, e)
            }
        }
    }
}
