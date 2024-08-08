package ru.numismatics.backend.common.repo

import ru.numismatics.backend.common.models.entities.Entity
import ru.numismatics.backend.common.repo.base.DbEntityResponseSuccess
import ru.numismatics.backend.common.repo.base.DbRequest
import ru.numismatics.backend.common.repo.base.IDbResponse
import ru.numismatics.backend.common.repo.base.IRepo

class MockRepo<T : Entity>(
    private val mockExec: (DbRequest<T>) -> IDbResponse = { DEFAULT_SUCCESS_EMPTY_MOCK },
//    private val mockSave: (Collection<T>) -> Collection<T> = { emptyList<T>() }
) : IRepo<T> {
    override suspend fun exec(request: DbRequest<T>): IDbResponse = mockExec.invoke(request)

    override fun save(values: Collection<T>): Collection<T> = emptyList()// mockSave.invoke(values)

    override fun clear() {}

    companion object {
        private val DEFAULT_SUCCESS_EMPTY_MOCK = DbEntityResponseSuccess(emptyList())
    }

}