package ru.numismatics.backend.repo.pg.test

actual fun getEnv(name: String): String? = System.getenv(name)

actual val properties = getPgProperties(getEnv("pgPort"))