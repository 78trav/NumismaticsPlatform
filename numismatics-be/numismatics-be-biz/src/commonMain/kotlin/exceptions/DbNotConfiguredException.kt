package ru.numismatics.backend.biz.exceptions

import ru.numismatics.backend.common.models.core.RequestType

class DbNotConfiguredException(requestType: RequestType) : Exception(
    "Database is not configured properly for mode $requestType"
)