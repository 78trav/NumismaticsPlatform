package ru.numismatics.backend.common.context

abstract class Processor<T>(protected val corSettings: CorSettings) {
    abstract suspend fun exec(context: T)
}
