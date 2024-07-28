package ru.numismatics.backend.common.mappers

import ru.numismatics.backend.common.models.core.Condition
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.stubs.Stubs

fun modeToInternal(value: String?) = when (value) {
    "prod" -> RequestType.PROD
    "test" -> RequestType.TEST
    "stub" -> RequestType.STUB
    else -> RequestType.PROD
}

fun stubCaseToInternal(value: String?) = when (value) {
    "success" -> Stubs.SUCCESS
    "notFound" -> Stubs.NOT_FOUND
    "badId" -> Stubs.BAD_ID
    "badName" -> Stubs.BAD_NAME
    "badDescription" -> Stubs.BAD_DESCRIPTION
    "badVisibility" -> Stubs.BAD_VISIBILITY
    "badSearch" -> Stubs.BAD_SEARCH
    "cannotCreate" -> Stubs.CANNOT_CREATE
    "cannotUpdate" -> Stubs.CANNOT_UPDATE
    "cannotDelete" -> Stubs.CANNOT_DELETE
    else -> Stubs.NONE
}

fun conditionToInternal(value: String?) = when (value) {
    "PF" -> Condition.PF
    "PL" -> Condition.PL
    "BU" -> Condition.BU
    "UNC" -> Condition.UNC
    "AU+" -> Condition.AU_PLUS
    "AU" -> Condition.AU
    "XF+" -> Condition.XF_PLUS
    "XF" -> Condition.XF
    "VF+" -> Condition.VF_PLUS
    "VF" -> Condition.VF
    "F" -> Condition.F
    "VG" -> Condition.VG
    "G" -> Condition.G
    "AG" -> Condition.AG
    "FA" -> Condition.FA
    "PR" -> Condition.PR
    else -> Condition.UNDEFINED
}