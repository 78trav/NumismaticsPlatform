package ru.numismatics.platform.libs.cor.core

/**
 * Блок кода, который обрабатывает контекст. Имеет имя и описание
 */
interface ICorExec<T> {
    val name: String
    val description: String
    suspend fun exec(context: T)
}
