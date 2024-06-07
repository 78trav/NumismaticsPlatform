package ru.numismatics.backend.api.references

import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.RequestType
import ru.numismatics.backend.common.models.core.stubs.Stubs
import ru.numismatics.backend.common.models.entities.EmptyEntity
import ru.numismatics.backend.common.models.id.CountryId
import ru.numismatics.backend.common.models.id.MaterialId
import ru.numismatics.backend.common.models.id.SectionId
import ru.numismatics.backend.common.models.entities.Material as MaterialInternal
import ru.numismatics.backend.common.models.entities.Country as CountryInternal
import ru.numismatics.backend.common.models.entities.Section as SectionInternal

fun NumismaticsPlatformContext.fromTransport(request: IReferenceRequest) {
    requestType = request.debug.toRequestType()
    stubCase = request.debug.toStubCase()

    command = when (request) {
        is ReferenceCreateRequest -> {
            fromTransportCreateUpdate(request.reference)
            Command.CREATE
        }

        is ReferenceReadRequest -> {
            fromTransportRead(request)
            Command.READ
        }

        is ReferenceUpdateRequest -> {
            fromTransportCreateUpdate(request.reference)
            Command.UPDATE
        }

        is ReferenceDeleteRequest -> {
            fromTransportDelete(request)
            Command.DELETE
        }
    }
}

private fun NumismaticsPlatformContext.fromTransportCreateUpdate(ref: IReference?) {
    entityType = when (ref) {
        is Country -> {
            entityRequest = ref.toInternal()
            EntityType.COUNTRY
        }

        is Material -> {
            entityRequest = ref.toInternal()
            EntityType.MATERIAL
        }

        is Section -> {
            entityRequest = ref.toInternal()
            EntityType.SECTION
        }

        else -> {
            entityRequest = EmptyEntity
            EntityType.UNDEFINED
        }
    }
}

private fun NumismaticsPlatformContext.fromTransportRead(request: ReferenceReadRequest) {
    entityType = when (request.referenceType) {
        ReferenceType.COUNTRY -> {
            entityRequest =
                if (request.idType == ReadIdType.SELF) CountryInternal(request.id) else CountryInternal.EMPTY
            EntityType.COUNTRY
        }

        ReferenceType.MATERIAL -> {
            entityRequest =
                if (request.idType == ReadIdType.SELF) MaterialInternal(request.id) else MaterialInternal.EMPTY
            EntityType.MATERIAL
        }

        ReferenceType.SECTION -> {
            entityRequest =
                if (request.idType == ReadIdType.SELF) SectionInternal(request.id) else SectionInternal(
                    id = SectionId.EMPTY,
                    parentId = SectionId.from(request.id)
                )
            EntityType.SECTION
        }
    }
}

private fun NumismaticsPlatformContext.fromTransportDelete(request: ReferenceDeleteRequest) {
    entityType = when (request.referenceType) {
        ReferenceType.COUNTRY -> {
            entityRequest = CountryInternal(request.id)
            EntityType.COUNTRY
        }

        ReferenceType.MATERIAL -> {
            entityRequest = MaterialInternal(request.id)
            EntityType.MATERIAL
        }

        ReferenceType.SECTION -> {
            entityRequest = SectionInternal(request.id)
            EntityType.SECTION
        }
    }
}

private fun Debug?.toRequestType() = when (this?.mode) {
    RequestDebugMode.PROD -> RequestType.PROD
    RequestDebugMode.TEST -> RequestType.TEST
    RequestDebugMode.STUB -> RequestType.STUB
    else -> RequestType.PROD
}

private fun Debug?.toStubCase() = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    RequestDebugStubs.BAD_NAME -> Stubs.BAD_NAME
    RequestDebugStubs.BAD_DESCRIPTION -> Stubs.BAD_DESCRIPTION
    RequestDebugStubs.BAD_VISIBILITY -> Stubs.BAD_VISIBILITY
    RequestDebugStubs.CANNOT_CREATE -> Stubs.CANNOT_CREATE
    RequestDebugStubs.CANNOT_UPDATE -> Stubs.CANNOT_UPDATE
    RequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    RequestDebugStubs.BAD_SEARCH -> Stubs.BAD_SEARCH
    else -> Stubs.NONE
}

private fun Material.toInternal() = MaterialInternal(
    id = MaterialId.from(id),
    name = name ?: "",
    description = description ?: "",
    probe = probe ?: 0f
)

private fun Country.toInternal() = CountryInternal(
    id = CountryId.from(id),
    name = name ?: "",
    description = description ?: ""
)

private fun Section.toInternal() = SectionInternal(
    id = SectionId.from(id),
    name = name ?: "",
    description = description ?: "",
    parentId = SectionId.from(parentId)
)
