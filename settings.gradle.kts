plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "ru-numismatics-platform"

listOf("numismatics-be", "numismatics-libs").forEach {
    includeBuild(it)
}
