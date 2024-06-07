import ru.numismatics.backend.api.refs.models.*
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.EntityPermission
import ru.numismatics.backend.common.models.core.EntityType
import ru.numismatics.backend.common.models.core.Error
import ru.numismatics.backend.common.models.entities.Country
import ru.numismatics.backend.common.models.entities.Material
import ru.numismatics.backend.common.models.entities.Section
import ru.numismatics.backend.common.models.id.CountryId
import ru.numismatics.backend.common.models.id.MaterialId
import ru.numismatics.backend.common.models.id.SectionId

open class ReferenceTest(protected val command: Command) {

    protected val debug = Debug(
        mode = RequestDebugMode.STUB,
        stub = RequestDebugStubs.SUCCESS
    )

    protected val error = Error(
        code = "err",
        group = "request",
        field = "name",
        message = "wrong name"
    )

    protected val perm = mutableSetOf(EntityPermission.READ, EntityPermission.UPDATE, EntityPermission.DELETE)

    protected val referencesExternal = mapOf(
        ReferenceType.MATERIAL to
                Material(
                    name = "Серебро 925",
                    description = "Серебро 925 пробы",
                    probe = 925f
                ),
        ReferenceType.COUNTRY to
                Country(
                    name = "Россия",
                    description = "Российская Федерация"
                ),
        ReferenceType.SECTION to
                Section(
                    name = "Сказки",
                    description = "Легенды и сказки народов России",
                    parentId = 7
                )
    )

    protected val referencesInternal = mapOf(
        EntityType.MATERIAL to
                Material(
                    id = MaterialId(2UL),
                    name = "Серебро 925",
                    description = "Серебро 925 пробы",
                    probe = 925f
                ).apply {
                    permissions.addAll(perm)
                },
        EntityType.COUNTRY to
                Country(
                    id = CountryId(3UL),
                    name = "Россия",
                    description = "Российская Федерация"
                ).apply {
                    permissions.addAll(perm)
                },
        EntityType.SECTION to
                Section(
                    id = SectionId(4UL),
                    name = "Сказки",
                    description = "Легенды и сказки народов России",
                    parentId = SectionId(5UL)
                ).apply {
                    permissions.addAll(perm)
                }
    )

}
