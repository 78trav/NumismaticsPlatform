package ru.numismatics.backend.api.references

import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.NumismaticsPlatformContext
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.Errors
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Entities
import ru.numismatics.backend.common.models.entities.toTransport
import ru.numismatics.backend.common.models.core.Error as ErrorInternal
import ru.numismatics.backend.common.models.exception.UnknownCommand
import ru.numismatics.backend.common.models.id.*
import ru.numismatics.backend.common.models.entities.Material as MaterialInternal
import ru.numismatics.backend.common.models.entities.Country as CountryInternal
import ru.numismatics.backend.common.models.entities.Section as SectionInternal
import ru.numismatics.backend.common.models.core.EntityPermission as PermissionInternal

fun NumismaticsPlatformContext.toTransport(): IReferenceResponse =
    if (command in setOf(Command.WS_INIT, Command.WS_CLOSE))
        wsInitCloseToTransport()
    else
        when (entityType) {
            EntityType.COUNTRY ->
                when (command) {
                    Command.CREATE -> countryCreateToTransport()
                    Command.READ -> countryReadToTransport()
                    Command.UPDATE -> countryUpdateToTransport()
                    Command.DELETE -> countryDeleteToTransport()
                    else -> throw UnknownCommand(command, entityType)
                }

            EntityType.MATERIAL ->
                when (command) {
                    Command.CREATE -> materialCreateToTransport()
                    Command.READ -> materialReadToTransport()
                    Command.UPDATE -> materialUpdateToTransport()
                    Command.DELETE -> materialDeleteToTransport()
                    else -> throw UnknownCommand(command, entityType)
                }

            EntityType.SECTION -> {
                when (command) {
                    Command.CREATE -> sectionCreateToTransport()
                    Command.READ -> sectionReadToTransport()
                    Command.UPDATE -> sectionUpdateToTransport()
                    Command.DELETE -> sectionDeleteToTransport()
                    else -> throw UnknownCommand(command, entityType)
                }
            }

            else -> throw UnknownCommand(command, entityType)

//    else -> {
//        when (command) {
//            Command.CREATE -> sectionCreateToTransport()
//            Command.READ -> sectionReadToTransport()
//            Command.UPDATE -> sectionUpdateToTransport()
//            Command.DELETE -> sectionDeleteToTransport()
//            else -> throw UnknownCommand(command, entityType)
//        }
//    }
////    } throw UnknownCommand(command, entityType)
        }

fun NumismaticsPlatformContext.countryCreateToTransport() = ReferenceCreateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.countryToTransport()
)

fun NumismaticsPlatformContext.countryUpdateToTransport() = ReferenceUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.countryToTransport()
)

fun NumismaticsPlatformContext.countryDeleteToTransport() = ReferenceDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.countryToTransport()
)

fun NumismaticsPlatformContext.countryReadToTransport() = ReferenceReadResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    items = entityResponse
        .filterIsInstance<CountryInternal>()
        .map { it.toTransport() }
        .takeIf { it.isNotEmpty() }
)

private fun Entities.countryToTransport() =
    try {
        this
            .filterIsInstance<CountryInternal>()
            .first()
            .toTransport()
    } catch (_: Exception) {
        null
    }

fun NumismaticsPlatformContext.materialCreateToTransport() = ReferenceCreateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.materialToTransport()
)

fun NumismaticsPlatformContext.materialUpdateToTransport() = ReferenceUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.materialToTransport()
)

fun NumismaticsPlatformContext.materialDeleteToTransport() = ReferenceDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.materialToTransport()
)

fun NumismaticsPlatformContext.materialReadToTransport() = ReferenceReadResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    items = entityResponse
        .filterIsInstance<MaterialInternal>()
        .map { it.toTransport() }
        .takeIf { it.isNotEmpty() }
)

private fun Entities.materialToTransport() =
    try {
        this
            .filterIsInstance<MaterialInternal>()
            .first()
            .toTransport()
    } catch (_: Exception) {
        null
    }

fun NumismaticsPlatformContext.sectionCreateToTransport() = ReferenceCreateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.sectionToTransport()
)

fun NumismaticsPlatformContext.sectionUpdateToTransport() = ReferenceUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.sectionToTransport()
)

fun NumismaticsPlatformContext.sectionDeleteToTransport() = ReferenceDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    item = entityResponse.sectionToTransport()
)

fun NumismaticsPlatformContext.sectionReadToTransport() = ReferenceReadResponse(
    result = state.toResult(),
    errors = errors.toTransport(),
    items = entityResponse
        .filterIsInstance<SectionInternal>()
        .map { it.toTransport() }
        .takeIf { it.isNotEmpty() }
)

fun NumismaticsPlatformContext.wsInitCloseToTransport() = ReferenceWSInitCloseResponse(
    result = state.toResult(),
    errors = errors.toTransport()
)

private fun Entities.sectionToTransport() =
    try {
        this
            .filterIsInstance<SectionInternal>()
            .first()
            .toTransport()
    } catch (_: Exception) {
        null
    }

fun Errors.toTransport() = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ErrorInternal.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun State.toResult() = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.FINISHING -> ResponseResult.SUCCESS
    else -> null
}

private fun MaterialInternal.toTransport() = ItemResponse(
    reference = Material(
        id = id.takeIf { it.isNotEmpty() }?.toLong(),
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        probe = probe.takeIf { it > 0 }
    ),
    permissions = permissions.toTransport { it.toTransport() }
)

private fun CountryInternal.toTransport() = ItemResponse(
    reference = Country(
        id = id.takeIf { it.isNotEmpty() }?.toLong(),
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() }
    ),
    permissions = permissions.toTransport { it.toTransport() }
)

private fun SectionInternal.toTransport() = ItemResponse(
    reference = Section(
        id = id.takeIf { it.isNotEmpty() }?.toLong(),
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        parentId = parentId.takeIf { it.isNotEmpty() }?.toLong(),
    ),
    permissions = permissions.toTransport { it.toTransport() }
)

fun PermissionInternal.toTransport() = when (this) {
    PermissionInternal.CREATE -> EntityPermission.CREATE
    PermissionInternal.READ -> EntityPermission.READ
    PermissionInternal.UPDATE -> EntityPermission.UPDATE
    PermissionInternal.DELETE -> EntityPermission.DELETE
}

