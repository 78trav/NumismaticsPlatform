package ru.numismatics.backend.api.v2.mapper

import kotlinx.serialization.json.Json

val v2Mapper = Json {
    ignoreUnknownKeys = true
}
