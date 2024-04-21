
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "com.otus.otuskotlin.marketplace"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {

    repositories {
        mavenCentral()
    }

    apply(plugin = "org.jetbrains.kotlin." + if (listOf("m2l3-kmp", "m2l4-1-interop", "m2l4-2-jni").any { it == name }) "multiplatform" else "jvm")

    group = rootProject.group
    version = rootProject.version
}
