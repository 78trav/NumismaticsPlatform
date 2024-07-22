package ru.numismatics.platform.libs.cor.core

@CorDSL
abstract class CorExecDSL<T> {
    abstract var name: String
    abstract var description: String

    protected var blockOn: suspend T.() -> Boolean = { false }

    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    fun on(blockOn: suspend T.() -> Boolean) {
        this.blockOn = blockOn
    }

    fun except(blockExcept: suspend T.(e: Throwable) -> Unit) {
        this.blockExcept = blockExcept
    }

    abstract fun build(): ICorExec<T>
}
