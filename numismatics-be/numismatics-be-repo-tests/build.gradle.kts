plugins {
    id("build-kmp")
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.coroutines.test)
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                implementation(project(":numismatics-be-common"))
                implementation(projects.numismaticsBeStubs)
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib"))
                api(kotlin("test-junit"))
            }
        }
    }
}