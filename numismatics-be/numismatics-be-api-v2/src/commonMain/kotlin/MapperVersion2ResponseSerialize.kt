package ru.numismatics.backend.api.v2

import ru.numismatics.backend.api.v2.mapper.v2Mapper
import ru.numismatics.backend.api.v2.models.ILotResponse

fun v2ResponseSerialize(obj: ILotResponse) =
    v2Mapper.encodeToString(ILotResponse.serializer(), obj)
