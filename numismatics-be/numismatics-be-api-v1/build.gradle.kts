plugins {
    id("build-jvm")
    alias(libs.plugins.openapi.generator)
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
    }
}

openApiGenerate {

    val openApiGroup = "${rootProject.group}.api.v1"

    generatorName.set("kotlin") // Это и есть активный генератор
    packageName.set(openApiGroup)
    apiPackage.set("$openApiGroup.api")
    modelPackage.set("$openApiGroup.models")
    invokerPackage.set("$openApiGroup.invoker")
    inputSpec.set(rootProject.ext["api-v1"] as String)
//    inputSpec.set(rootProject.ext["api-refs"] as String)

//    outputDir.set("$projectDir")

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions =
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )

}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)
    implementation(project(":numismatics-be-common"))
    testImplementation(kotlin("test"))
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
