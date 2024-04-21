
kotlin {

    linuxX64() {
        compilations.getByName("main") {
            cinterops {
                // настраиваем cinterop в файле src/nativeInterop/cinterop/libcurl.def
                val libcurl by creating
//                    create("libcurl")
            }
        }
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

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
        val commonTest by getting {

            val coroutinesVersion = libs.versions.coroutines.get()

            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }
        // С 1.9.20 можно так
        nativeMain {
        }
        nativeTest {
            dependencies {
                implementation(libs.test.junit)
            }
        }

    }
}
