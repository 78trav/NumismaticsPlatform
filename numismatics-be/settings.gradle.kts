rootProject.name = "numismatics-backend"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

listOf(
//    "tmp",
    "api-v1",
    "api-v2",
    "api-v2-mapper",
//    "api-references",
//    "api-marketprice",
    "common",
    "ktor",
    "biz",
    "stubs",
    "repo-mem",
    "repo-pg",
    "repo-tests"
).forEach {
    include(":numismatics-be-$it")
}