package ru.numismatics.platform.libs.cor.operation

import ru.numismatics.platform.libs.cor.core.CorDSL
import ru.numismatics.platform.libs.cor.core.CorExecDSL
import ru.numismatics.platform.libs.cor.core.ICorExec
import ru.numismatics.platform.libs.cor.work.CorJobDSL

@CorDSL
class CorOperationDSL<T> : CorExecDSL<T>() {

    override var name = ""
    override var description = ""

    private val jobs: MutableList<CorExecDSL<T>> = mutableListOf()

    fun add(job: CorExecDSL<T>) {
        jobs.add(job)
    }

    override fun build(): ICorExec<T> = CorOperation(
        name = name,
        description = description,
        jobs = jobs.map { it.build() },
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}

/**
 * Создает операцию, элементы которой исполняются последовательно.
 */
fun <T> CorOperationDSL<T>.operation(function: CorOperationDSL<T>.() -> Unit) {
    add(
        CorOperationDSL<T>().apply { function() }
    )
}

/**
 * Создает работу
 */
fun <T> CorOperationDSL<T>.job(function: CorJobDSL<T>.() -> Unit) {
    add(
        CorJobDSL<T>().apply { function() }
    )
}

/**
 * Создает работу с on и except по умолчанию
 */
fun <T> CorOperationDSL<T>.job(
    name: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(
        CorJobDSL<T>().apply {
            this.name = name
            this.description = description
            this.handle(blockHandle)
            on { true }
        }
    )
}

/**
 * Точка входа в DSL построения операций.
 * Элементы исполняются последовательно.
 */
fun <T> process(function: CorOperationDSL<T>.() -> Unit): CorOperationDSL<T> = CorOperationDSL<T>().apply {
    on { true }
    function()
}
