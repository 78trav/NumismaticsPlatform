
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.numismatics.platform"
version = "0.1"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

subprojects {
    repositories {
        mavenCentral()
    }
//    group = rootProject.group
//    version = rootProject.version
}

ext {
    set("version", version)
}