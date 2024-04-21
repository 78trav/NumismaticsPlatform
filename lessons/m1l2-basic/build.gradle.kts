plugins {
    application
}

application {
    mainClass.set("ru.otus.otuskotlin.m1l2.MainKt")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.test.junit)
}
