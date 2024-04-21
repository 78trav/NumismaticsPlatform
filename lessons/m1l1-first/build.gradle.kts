
dependencies {
    testImplementation(libs.test.junit)
}

kotlin {
    jvmToolchain(libs.versions.jvm.compiler.get().toInt())
}
