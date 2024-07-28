package ru.numismatics.backend.common

interface IProcessor {
    suspend fun exec(context: NumismaticsPlatformContext)
}