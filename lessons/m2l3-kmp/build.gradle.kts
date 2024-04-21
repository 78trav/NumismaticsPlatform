plugins {
    // Только для lombock!!
    java
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = libs.versions.jvm.compiler.get()
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    linuxX64()

    // Description of modules corresponding to our target platforms
    //  common - common code that we can use on different platforms
    //  for each target platform, we can specify our own specific dependencies
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.datetime)
            }
        }
        commonTest {

            val coroutinesVersion = libs.versions.coroutines.get()

            dependencies {
                implementation(libs.test.junit)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        jvmMain {
        }
        jvmTest {
            dependencies {
                implementation(libs.test.junit)
            }
        }
        // С 1.9.20 можно так
        nativeMain {
        }
        nativeTest {
        }
    }
}

// Только для lombock!!
dependencies {
    val lombokVer = "1.18.20"
    compileOnly("org.projectlombok:lombok:${lombokVer}")
    annotationProcessor("org.projectlombok:lombok:${lombokVer}")
}
