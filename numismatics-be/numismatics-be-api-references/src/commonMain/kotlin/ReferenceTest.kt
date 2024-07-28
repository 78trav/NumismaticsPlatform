package ru.numismatics.backend.api.references

import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.Error

abstract class ReferenceTest(protected val command: Command) {

    companion object {

        val debug = Debug(
            mode = RequestDebugMode.STUB,
            stub = RequestDebugStubs.SUCCESS
        )

        val error = Error(
            code = "err",
            group = "request",
            field = "name",
            message = "wrong name"
        )

        val material = Material(
            name = "Серебро 925",
            description = "Серебро 925 пробы",
            probe = 925f
        )

        val country = Country(
            name = "Россия",
            description = "Российская Федерация"
        )

        val section = Section(
            name = "Сказки",
            description = "Легенды и сказки народов России",
            parentId = 7
        )
    }
}
