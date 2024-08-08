package ru.numismatics.backend.biz.validation

import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.context.fail
import ru.numismatics.backend.common.helpers.errorValidation
import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.platform.libs.cor.operation.CorOperationDSL
import ru.numismatics.platform.libs.cor.operation.job

internal fun <T : Entity> CorOperationDSL<NumismaticsPlatformContext<T>>.validationNameHasContent(name: String) = job {
    this.name = name
    description = """
        Проверка на наличие слов в наименовании.
        Отказ в случае наличия только бессмысленных символов типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")

    on { entityValidating.name.isNotEmpty() && !entityValidating.name.contains(regExp) }

    handle {
        fail(
            errorValidation(
                field = "name",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}