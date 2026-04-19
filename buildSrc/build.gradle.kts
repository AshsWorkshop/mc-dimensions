import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

plugins {
    `kotlin-dsl`
}
apply(from = "${rootDir.parentFile}/properties.gradle.kts")

repositories {
    mavenLocal()
    maven {
        name = "NeoForged Maven"
        url = uri("https://maven.neoforged.net/releases")
    }
    maven {
        name = "Fabric Maven"
        url = uri("https://maven.fabricmc.net/")
    }
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("net.neoforged:moddev-gradle:${rootProject.extra["neoforgeGradle"]}")
    implementation("net.fabricmc:fabric-loom:${rootProject.extra["fabricLoom"]}")
    implementation("io.github.wasabithumb:jtoml:${rootProject.extra["gradleJtoml"]}")
    implementation("io.github.wasabithumb:jtoml-kotlin:${rootProject.extra["gradleJtoml"]}")
    implementation("de.undercouch.download:de.undercouch.download.gradle.plugin:${rootProject.extra["gradleDownload"]}")
}
