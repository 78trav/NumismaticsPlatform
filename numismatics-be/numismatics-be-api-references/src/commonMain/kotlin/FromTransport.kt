package ru.numismatics.backend.api.references

import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.mappers.modeToInternal
import ru.numismatics.backend.common.mappers.stubCaseToInternal
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.entities.EmptyEntity
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.models.entities.Material as MaterialInternal
import ru.numismatics.backend.common.models.entities.Country as CountryInternal
import ru.numismatics.backend.common.models.entities.Section as SectionInternal

fun NumismaticsPlatformContext.fromTransport(request: IReferenceRequest) {
    requestType = modeToInternal(request.debug?.mode?.value)
    stubCase = stubCaseToInternal(request.debug?.stub?.value)

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
                    parentId = request.id.toSectionId()
                )
            EntityType.SECTION
        }

        else -> EntityType.UNDEFINED
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

        else -> EntityType.UNDEFINED
    }
}

private fun Material.toInternal() = MaterialInternal(
    id = id.toMaterialId(),
    name = name ?: "",
    description = description ?: "",
    probe = probe ?: 0f
)

private fun Country.toInternal() = CountryInternal(
    id = id.toCountryId(),
    name = name ?: "",
    description = description ?: ""
)

private fun Section.toInternal() = SectionInternal(
    id = id.toSectionId(),
    name = name ?: "",
    description = description ?: "",
    parentId = parentId.toSectionId()
)

