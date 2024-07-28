package ru.numismatics.backend.common.models.core

enum class Command {
    NONE,
    CREATE,
    READ,
    UPDATE,
    DELETE,
    SEARCH,
    WS_INIT,
    WS_CLOSE
}