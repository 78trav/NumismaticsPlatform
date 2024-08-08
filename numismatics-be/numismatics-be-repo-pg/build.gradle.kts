import com.bmuschko.gradle.docker.tasks.container.*
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage
import com.github.dockerjava.api.command.InspectContainerResponse
import com.github.dockerjava.api.model.ExposedPort
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

plugins {
    id("build-kmp")
    alias(libs.plugins.muschko.remote)
    alias(libs.plugins.liquibase)
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(project(":numismatics-be-common"))

                implementation(libs.kotlinx.coroutines)
                implementation(libs.uuid)

                implementation(projects.numismaticsBeStubs)

                implementation(libs.db.postgres)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.numismaticsBeStubs)
                implementation(projects.numismaticsBeRepoTests)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(libs.ktorm.core)
                implementation(libs.ktorm.pg)
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        linuxX64Main {
            dependencies {
                implementation(kotlin("stdlib"))
//                implementation(libs.db.postgres.pgkn)
                implementation(libs.valid)
            }
        }
    }

    linuxX64 {
        compilations.getByName("main") {
            cinterops {
                val libpq by creating {
                    defFile = File("$projectDir/src/linuxX64Main/kotlin/libpq.def")
                }
            }
        }
    }

}

//tasks {
//    withType<KotlinCompile> {
//        kotlinOptions {
//            freeCompilerArgs += listOf("-Xskip-prerelease-check")
//        }
//    }
//}

//tasks.compileKotlinLinuxX64 {
//    kotlinOptions {
//        freeCompilerArgs += "-Xskip-prerelease-check"
//    }
//}

dependencies {
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.liquibase.snakeyml)
    liquibaseRuntime(libs.db.postgres)
}


tasks {
    val taskGroup = "pgContainer"

    var pgPort = 5432
    val pgDbName = "numismatics"
    val pgUsername = "postgres"
    val pgPassword = "pg2k24"

    // Здесь в тасках запускаем PostgreSQL в контейнере
    // Накатываем liquibase миграцию
    // Передаем настройки в среду тестирования
    val pullImage by creating(DockerPullImage::class) {
        group = taskGroup
        image.set("postgres:15.6")
    }
    val dbContainer by creating(DockerCreateContainer::class) {
        group = taskGroup
        dependsOn(pullImage)
        targetImageId(pullImage.image)
        withEnvVar("POSTGRES_PASSWORD", pgPassword)
        withEnvVar("POSTGRES_USER", pgUsername)
        withEnvVar("POSTGRES_DB", pgDbName)
        healthCheck.cmd("pg_isready")
        hostConfig.portBindings.set(listOf(":5432"))
        exposePorts("tcp", listOf(5432))
        hostConfig.autoRemove.set(true)
    }
    val stopPg by creating(DockerStopContainer::class) {
        group = taskGroup
        targetContainerId(dbContainer.containerId)
    }
    val startPg by creating(DockerStartContainer::class) {
        group = taskGroup
        dependsOn(dbContainer)
        targetContainerId(dbContainer.containerId)
        finalizedBy(stopPg)
    }
    val inspectPg by creating(DockerInspectContainer::class) {
        group = taskGroup
        dependsOn(startPg)
        finalizedBy(stopPg)
        targetContainerId(dbContainer.containerId)
        onNext(
            object : Action<InspectContainerResponse> {
                override fun execute(container: InspectContainerResponse) {
                    pgPort = container.networkSettings.ports.bindings[ExposedPort.tcp(5432)]
                        ?.first()
                        ?.hostPortSpec
                        ?.toIntOrNull()
                        ?: throw Exception("Postgres port is not found in container")
                }
            }
        )
    }
    val liquibaseUpdate = getByName("update") {
        group = taskGroup
        dependsOn(inspectPg)
        finalizedBy(stopPg)
        doFirst {
            println("waiting for a while ${System.currentTimeMillis() / 1000000}")
            Thread.sleep(7000)
            println("LQB: \"jdbc:postgresql://localhost:$pgPort/$pgDbName\" ${System.currentTimeMillis() / 1000000}")
            liquibase {
                activities {
                    register("main") {
                        arguments = mapOf(
                            "logLevel" to "info",
                            "searchPath" to layout.projectDirectory.dir("migrations").asFile.toString(),
                            "changelogFile" to "changelog-v0.0.1.sql",
                            "url" to "jdbc:postgresql://localhost:$pgPort/$pgDbName",
                            "username" to pgUsername,
                            "password" to pgPassword,
                            "driver" to "org.postgresql.Driver"
                        )
                    }
                }
            }
        }
    }
    val waitPg by creating(DockerWaitContainer::class) {
        group = taskGroup
        dependsOn(inspectPg)
        dependsOn(liquibaseUpdate)
        containerId.set(startPg.containerId)
        finalizedBy(stopPg)
        doFirst {
            println("PORT: $pgPort")
        }
    }
    withType(KotlinNativeTest::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("pgPort", pgPort.toString())
        }
    }
    withType(Test::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("pgPort", pgPort.toString())
        }
    }
}