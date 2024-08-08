package ru.numismatics.platform.libs.cor.work

import ru.numismatics.platform.libs.cor.core.*

@CorDSL
class CorJobDSL<T> : CorExecDSL<T>() {

    override var name = ""
    override var description = ""

    private var blockHandle: suspend T.() -> Unit = {}

    fun handle(blockHandle: suspend T.() -> Unit) {
        this.blockHandle = blockHandle
    }

    override fun build(): ICorExec<T> = CorJob(
        name = name,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )

}
