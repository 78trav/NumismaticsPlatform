plugins {
    id("build-kmp")
}

//group = "${rootProject.group}.common"
println("$name group: $group, version: $version")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api(libs.kotlinx.datetime)
                implementation(libs.valid)
                implementation(libs.kotlinx.coroutines)

            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
