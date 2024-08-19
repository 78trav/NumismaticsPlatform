import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("build-kmp")
    alias(libs.plugins.crowdproj.generator)
    alias(libs.plugins.kotlinx.serialization)
}

crowdprojGenerate {
    packageName.set("${rootProject.group}.api.refs")
    inputSpec.set(rootProject.ext["api-refs"] as String)
}

kotlin {
    sourceSets {
        val serializationVersion: String by project

        commonMain {
            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/src/commonMain/kotlin"))
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(project(":numismatics-be-common"))
                implementation(project(":numismatics-be-api-v2-mapper"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(project(":numismatics-be-stubs"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {

    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
        outputDir.set(layout.buildDirectory.file("generate-resources").get().toString())
        finalizedBy("compileCommonMainKotlinMetadata")
    }

    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerateTask)
    }
}
