package ru.numismatics.backend.common.context

import ru.numismatics.backend.common.models.entities.Entity

abstract class Processor<T : Entity>(protected val corSettings: CorSettings<T>) {
    abstract suspend fun exec(context: NumismaticsPlatformContext<T>)
}
