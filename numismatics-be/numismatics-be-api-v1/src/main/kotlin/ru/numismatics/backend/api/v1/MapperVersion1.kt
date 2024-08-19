package ru.numismatics.backend.api.v1

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import ru.numismatics.backend.api.v1.models.ILotResponse

val v1Mapper: JsonMapper = JsonMapper.builder().apply {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
}.build().apply {
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
}

fun v1ResponseSerialize(response: ILotResponse): String = v1Mapper.writeValueAsString(response)
