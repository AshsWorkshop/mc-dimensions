import net.ashwork.gradle.multiloader.resolveProperty

plugins {
    java
    idea
    id("multiloader-publishing")
}

project.extra["mod_version"] = "${resolveProperty("mod_version")}.${resolveProperty("mod_version_patch")}"
version = "${resolveProperty("mod_version")}+${resolveProperty("vanillaMinecraft")}"

java {
    withSourcesJar()
    toolchain.languageVersion.set(
        JavaLanguageVersion.of(resolveProperty("java_version"))
    )
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    if (options is StandardJavadocDocletOptions) {
        (options as StandardJavadocDocletOptions).tags(
            "extension:f:Access extension from: "
        )
    }
}

idea.module {
    isDownloadJavadoc = true
    isDownloadSources = true
}

configurations.all {
    resolutionStrategy {
        cacheDynamicVersionsFor(10, TimeUnit.MINUTES)
        cacheChangingModulesFor(10, TimeUnit.MINUTES)
    }
}

repositories {
    mavenCentral()
    maven {
        name = "NeoForged Maven"
        url = uri("https://maven.neoforged.net/releases")
    }
    maven {
        name = "Fabric Maven"
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "Ash's Multiloader"
        url = uri("https://maven.pkg.github.com/AshsWorkshop/mc-multiloader")
        credentials {
            username = project.findProperty("gh.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gh.packages.token") as String? ?: System.getenv("TOKEN")
        }
    }
}
