package ru.numismatics.backend.api.references

import ru.numismatics.backend.api.refs.models.IReferenceResponse
import ru.numismatics.backend.api.v2.mapper.v2Mapper

fun v2ResponseSerialize(obj: IReferenceResponse) =
    v2Mapper.encodeToString(IReferenceResponse.serializer(), obj)
