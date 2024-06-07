import org.jetbrains.kotlin.util.collectionUtils.concat

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.numismatics.backend"
version = "0.1"

println("$name group: $group, version: $version")

//subprojects {
//    group = rootProject.group
//    version = rootProject.version
//}

//subprojects.forEach {
//    it.group = "${rootProject.group}." + it.name.split("-").drop(2).joinToString(".")
//    it.version = rootProject.version
//    println("${it.name} ${it.group} ${it.version}")
//}

allprojects {
    repositories {
        mavenCentral()
    }
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("api-refs", specDir.file("api-references.yaml").toString())
    set("api-v1", specDir.file("api-v1.yaml").toString())
    set("api-v2", specDir.file("api-v2.yaml").toString())
}

tasks {
    arrayOf("build", "clean", "check").forEach { tsk ->
        create(tsk) {
            group = "build"
            dependsOn(subprojects.map { it.getTasksByName(tsk, false) })
        }
    }
}
