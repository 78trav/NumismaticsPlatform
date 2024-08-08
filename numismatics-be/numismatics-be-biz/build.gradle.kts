plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":numismatics-be-common"))
                implementation(project(":numismatics-be-stubs"))

                implementation(libs.cor)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.kotlinx.coroutines.test)
                implementation(projects.numismaticsBeStubs)
                implementation(projects.numismaticsBeRepoMem)


//                api(libs.coroutines.test)
            }
        }
        jvmMain {
            dependencies {
//                implementation(kotlin("stdlib-jdk8"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
