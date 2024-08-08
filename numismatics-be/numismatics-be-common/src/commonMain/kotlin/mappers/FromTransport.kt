package ru.numismatics.backend.common.mappers

import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.stubs.Stubs
import ru.numismatics.platform.libs.validation.ValidationEr
import ru.numismatics.platform.libs.validation.ValidationOk
import ru.numismatics.platform.libs.validation.ValidationResult

fun String?.toMode(): ValidationResult<RequestType, Error> = when (this?.uppercase()) {
    "PROD" -> ValidationOk(RequestType.PROD)
    "TEST" -> ValidationOk(RequestType.TEST)
    "STUB" -> ValidationOk(RequestType.STUB)
    null -> ValidationOk(RequestType.PROD)
    else -> ValidationEr(
        Error(
            code = "wrong-mode",
            group = "mapper-validation",
            field = "debug.mode",
            message = "Unsupported value for mode \"$this\""
        )
    )
}

fun String?.toStubCase(): ValidationResult<Stubs, Error> = when (this) {
    "success" -> ValidationOk(Stubs.SUCCESS)
    "notFound" -> ValidationOk(Stubs.NOT_FOUND)
    "badId" -> ValidationOk(Stubs.BAD_ID)
    "badName" -> ValidationOk(Stubs.BAD_NAME)
    "badDescription" -> ValidationOk(Stubs.BAD_DESCRIPTION)
    "badVisibility" -> ValidationOk(Stubs.BAD_VISIBILITY)
    "badSearch" -> ValidationOk(Stubs.BAD_SEARCH)
    "cannotCreate" -> ValidationOk(Stubs.CANNOT_CREATE)
    "cannotUpdate" -> ValidationOk(Stubs.CANNOT_UPDATE)
    "cannotDelete" -> ValidationOk(Stubs.CANNOT_DELETE)
    null -> ValidationOk(Stubs.NONE)
    else -> ValidationEr(
        Error(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

fun String?.toCondition(): ValidationResult<Condition, Error> = when (this?.uppercase()) {
    "PF" -> ValidationOk(Condition.PF)
    "PL" -> ValidationOk(Condition.PL)
    "BU" -> ValidationOk(Condition.BU)
    "UNC" -> ValidationOk(Condition.UNC)
    "AU+" -> ValidationOk(Condition.AU_PLUS)
    "AU" -> ValidationOk(Condition.AU)
    "XF+" -> ValidationOk(Condition.XF_PLUS)
    "XF_PLUS" -> ValidationOk(Condition.XF_PLUS)
    "XF" -> ValidationOk(Condition.XF)
    "VF+" -> ValidationOk(Condition.VF_PLUS)
    "VF_PLUS" -> ValidationOk(Condition.VF_PLUS)
    "VF" -> ValidationOk(Condition.VF)
    "F" -> ValidationOk(Condition.F)
    "VG" -> ValidationOk(Condition.VG)
    "G" -> ValidationOk(Condition.G)
    "AG" -> ValidationOk(Condition.AG)
    "FA" -> ValidationOk(Condition.FA)
    "PR" -> ValidationOk(Condition.PR)
    null -> ValidationOk(Condition.UNDEFINED)
    else -> ValidationEr(
        Error(
            code = "wrong-condition",
            group = "mapper-validation",
            field = "condition",
            message = "Unsupported value for condition \"$this\""
        )
    )
}
