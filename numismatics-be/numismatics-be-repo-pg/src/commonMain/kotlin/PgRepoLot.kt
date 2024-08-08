package ru.numismatics.backend.repo.pg

import com.benasher44.uuid.uuid4
import ru.numismatics.backend.common.models.entities.Lot
import ru.numismatics.backend.common.repo.base.RepoBase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PgRepoLot(
    properties: PgProperties,
    randomUuid: () -> String = { uuid4().toString() }
) : RepoBase<Lot> {
    override fun clear()
}