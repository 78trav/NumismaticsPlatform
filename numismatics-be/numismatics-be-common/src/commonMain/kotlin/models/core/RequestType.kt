package ru.numismatics.backend.common.models.core

enum class RequestType(val configName: String) {
    PROD("prod"),
    TEST("test"),
    STUB("stub")
}
