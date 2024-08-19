import io.ktor.plugin.features.*

plugins {
    id("build-kmp")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.muschko.remote)
}

println("$name group: $group, version: $version")

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    configureNativeImage(project)
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

jib {
    container.mainClass = application.mainClass.get()
}

kotlin {
    // !!! Обязательно. Иначе не проходит сборка толстых джанриков в shadowJar
    jvm { withJava() }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "ru.numismatics.platform.app.ktor.main"
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.yaml)
                implementation(libs.ktor.server.negotiation)
                implementation(libs.ktor.server.headers.response)
                implementation(libs.ktor.server.headers.caching)
                implementation(libs.ktor.server.websocket)
//                implementation(libs.ktor.server.double.receive)

//                // Для того, чтоб получать содержимое запроса более одного раза
//                В Application.main добавить `install(DoubleReceive)`
//                implementation("io.ktor:ktor-server-double-receive:${libs.versions.ktor.get()}")

                implementation(project(":numismatics-be-common"))
                implementation(project(":numismatics-be-biz"))
//                implementation(project(":ok-marketplace-app-common"))

                // v2 api
                implementation(project(":numismatics-be-api-references"))
                implementation(project(":numismatics-be-api-marketprice"))
                implementation(project(":numismatics-be-api-v2"))
                implementation(project(":numismatics-be-api-v2-mapper"))

//                // Stubs
                implementation(project(":numismatics-be-stubs"))
//                // RabbitMQ
////                implementation(project(":ok-marketplace-app-rabbit"))

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.serialization.json)

//                // logging
//                implementation(project(":ok-marketplace-api-log1"))
//                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-common")
//                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-kermit")
//                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-socket")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)

//                implementation(project(":numismatics-be-api-v2-mapper"))
            }
        }

        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                // jackson
                implementation(libs.ktor.serialization.jackson)
//                implementation(libs.ktor.server.calllogging)
                implementation(libs.ktor.server.headers.default)

//                implementation(libs.logback)

                // transport models
                implementation(project(":numismatics-be-api-references"))
                implementation(project(":numismatics-be-api-marketprice"))
                implementation(project(":numismatics-be-api-v1"))

//                implementation("ru.otus.otuskotlin.marketplace.libs:ok-marketplace-lib-logging-logback")

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
    shadowJar {
        isZip64 = true
    }

    // Если ошибка: "Entry application.yaml is a duplicate but no duplicate handling strategy has been set."
    // Возникает из-за наличия файлов как в common, так и в jvm платформе
    withType(ProcessResources::class) {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

}
