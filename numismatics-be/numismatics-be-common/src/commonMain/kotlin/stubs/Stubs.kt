package ru.numismatics.backend.common.stubs

import ru.numismatics.backend.common.models.core.Error

enum class Stubs {
    NONE,
    SUCCESS,
    NOT_FOUND,
    BAD_ID,
    BAD_NAME,
    BAD_DESCRIPTION,
    BAD_VISIBILITY,
    BAD_SEARCH,
    CANNOT_CREATE,
    CANNOT_UPDATE,
    CANNOT_DELETE,
    DB_ERROR,
    OTHER_ERROR
}

fun Stubs.toError() =
    when (this) {
        Stubs.NONE -> Error.NO_ERROR
        Stubs.SUCCESS -> Error.NO_ERROR
        Stubs.NOT_FOUND -> Error(
            code = "execute-not-found",
            group = "execute",
            message = "Сущность не найдена"
        )

        Stubs.BAD_ID -> Error(
            code = "validation-id",
            group = "validation",
            field = "Идентификатор",
            message = "Неправильный идентификатор"
        )

        Stubs.BAD_NAME -> Error(
            code = "validation-name",
            group = "validation",
            field = "Имя",
            message = "Неправильное имя"
        )

        Stubs.BAD_DESCRIPTION -> Error(
            code = "validation-description",
            group = "validation",
            field = "Описание",
            message = "Неправильное описание"
        )

        Stubs.BAD_VISIBILITY -> Error(
            code = "validation-visibility",
            group = "validation",
            message = "Неправильное видимость"
        )

        Stubs.BAD_SEARCH -> Error(
            code = "validation-search",
            group = "validation",
            message = "Неправильные условия поиска"
        )

        Stubs.CANNOT_CREATE -> Error(
            code = "permission-create",
            group = "permission",
            message = "Нет прав на создание"
        )

        Stubs.CANNOT_UPDATE -> Error(
            code = "permission-update",
            group = "permission",
            message = "Нет прав на изменение"
        )

        Stubs.CANNOT_DELETE -> Error(
            code = "permission-delete",
            group = "permission",
            message = "Нет прав на удаление"
        )

        Stubs.DB_ERROR -> Error(
            code = "system-db-error",
            group = "system",
            message = "Ошибка базы данных"
        )

        Stubs.OTHER_ERROR -> Error(
            code = "system-other",
            group = "system",
            message = "Неклассифицированная ошибка"
        )
    }
