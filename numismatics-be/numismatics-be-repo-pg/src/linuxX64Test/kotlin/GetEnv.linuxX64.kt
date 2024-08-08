package ru.numismatics.backend.repo.pg.test

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual fun getEnv(name: String): String? = getenv(name)?.toKString()

actual val properties = getPgProperties(getEnv("pgPort"))