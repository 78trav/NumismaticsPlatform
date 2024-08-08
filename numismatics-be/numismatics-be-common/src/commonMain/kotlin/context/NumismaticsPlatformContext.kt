package ru.numismatics.backend.common.context

import kotlinx.datetime.Instant
import ru.numismatics.backend.common.NONE
import ru.numismatics.backend.common.models.core.*
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.models.id.RequestId
import ru.numismatics.backend.common.repo.base.IRepo
import ru.numismatics.backend.common.ws.IWsSession

data class NumismaticsPlatformContext<T : Entity>(

    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: Errors = mutableListOf(),

    var corSettings: CorSettings<T> = CorSettings(),

    var repo: IRepo<T>? = null,

    var requestType: RequestType = RequestType.TEST,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.EMPTY,
    val timeStart: Instant = Instant.NONE,

    var entityRequest: T,

    var entityValidating: T = entityRequest,
    var entityValidated: T = entityRequest,

    var entityRepoRead: T = entityRequest, // То, что прочитали из репозитория
    var entityRepoPrepare: T = entityRequest, // То, что готовим для сохранения в БД
    var entityRepoDone: MutableList<T> = mutableListOf(),  // Результат, полученный из БД

    val entityResponse: MutableList<T> = mutableListOf(),

    var wsSession: IWsSession = IWsSession.NONE
)

inline fun <T : Entity> NumismaticsPlatformContext<T>.addError(error: Error) {
    errors.add(error)
}

inline fun <T : Entity> NumismaticsPlatformContext<T>.addErrors(error: Collection<Error>) {
    errors.addAll(error)
}

inline fun <T : Entity> NumismaticsPlatformContext<T>.fail(error: Error) {
    addError(error)
    state = State.FAILING
}

inline fun <T : Entity> NumismaticsPlatformContext<T>.fail(errors: Collection<Error>) {
    addErrors(errors)
    state = State.FAILING
}

inline fun <reified T : Entity> NumismaticsPlatformContext<T>.wrongCommand() =
    Error(
        code = "validation-command-badValue",
        group = "validation",
        message = "Команда $command для сущности ${T::class.simpleName} не поддерживается"
    )
