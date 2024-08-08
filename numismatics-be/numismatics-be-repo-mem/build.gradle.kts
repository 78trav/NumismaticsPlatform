plugins {
    id("build-kmp")
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":numismatics-be-common"))

                implementation(libs.kotlinx.coroutines)
                implementation(libs.db.cache4k)
                implementation(libs.uuid)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.numismaticsBeRepoTests)
                implementation(projects.numismaticsBeStubs)
//                implementation(libs.kotlinx.coroutines.test)
            }
        }
//        jvmMain {
//            dependencies {
//                implementation(kotlin("stdlib"))
//            }
//        }
//        jvmTest {
//            dependencies {
//                implementation(kotlin("test-junit"))
//            }
//        }
    }
}
