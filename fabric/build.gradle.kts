import groovy.json.JsonOutput
import net.fabricmc.loom.configuration.ide.idea.IdeaSyncTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import net.ashwork.gradle.multiloader.*

plugins {
    id("multiloader-base")
    id("net.fabricmc.fabric-loom")
}

internal val main: Project = rootProject.project("${providers.gradleProperty("mod_id").get()}-common")

// Create source sets
val common = configureInheritingFeature("common", "common:common")
val client = configureInheritingFeature("client", "common", "common:client")
val data = configureInheritingFeature("data", "common", "client", "common:data", publish = true, bundle = listOf("common:data"), depend = listOf("main"))

configureInheritingFeature("main", "common", "client", "common:common", "common:client", publish = true, bundle = listOf("common", "client", "common:common", "common:client"), excludeClasspathDependencies = true)

// `excludeClasspathDependencies` lets us do this
configurations.named("commonCompileClasspath") { extendsFrom(configurations.compileClasspath) }
configurations.named("commonRuntimeClasspath") { extendsFrom(configurations.runtimeClasspath) }

tasks.named("compileJava") {
    dependsOn(tasks.named("compileDataJava"))
}

internal val generated: SourceSet = sourceSets.create("generated") {
    java.setSrcDirs(emptyList<Any>())
}

val commonImplementation by configurations.getting

dependencies {
    minecraft("com.mojang:minecraft:${resolveProperty("vanillaMinecraft")}")
    // This needs to also be present in "main" so that loom sets up loader properly, as it is hard-coded to "main".
    implementation(commonImplementation("net.fabricmc:fabric-loader:${resolveProperty("fabricLoader")}")!!)
    commonImplementation("net.fabricmc.fabric-api:fabric-api:${resolveProperty("fabricApi")}")
    commonImplementation(platform("net.ashwork.mc:ashsmultiloader:${resolveProperty("vanillaMinecraft")}.+"))
    commonImplementation("net.ashwork.mc:ashsmultiloader-fabric") {
        capabilities {
            requireFeature("data")
        }
    }
}

fun generateModFile(name: String = "", dependsOn: Pair<String, String>? = null): TaskProvider<Task> {
    return tasks.register("generate${if (name.isEmpty()) "" else name.replaceFirstChar { it.uppercase() }}ModFile") {
        // Dependencies
        val dependencies = mutableMapOf(
            "java" to resolveProperty("java_version"),
            "minecraft" to "~${resolveProperty("vanillaMinecraft")}",
            "fabricloader" to ">=${resolveProperty("fabricLoader")}",
            "fabric-api" to ">=${resolveProperty("fabricApi")}"
        )
        if (dependsOn != null) {
            dependencies[dependsOn.first] = dependsOn.second
        }

        // Base json
        val modsJson: Map<String, Any> = mapOf(
            "schemaVersion" to 1,
            "id" to "${resolveProperty("mod_id")}${if (name.isEmpty()) "" else "_${name}"}",
            "version" to resolveProperty("mod_version"),
            "name" to "${resolveProperty("mod_name")} (${project.name}${if (name.isEmpty()) "" else "-${name}"})",
            "description" to resolveProperty("mod_description"),
            "authors" to resolveProperty("mod_authors").split(",").map { it.trim() },
            "license" to resolveProperty("mod_license"),
            "environment" to "*",
            "entrypoints" to mapOf(
                "main" to listOf(
                    listOf(
                        resolveProperty("mod_group"),
                        resolveProperty("mod_subpackage"),
                        project.projectDir.name,
                        "${project.projectDir.name.capitalizeWords()}${resolveProperty("mod_subpackage").capitalizeWords()}"
                    ).joinToString(".")
                ),
                "fabric-datagen" to listOf(
                    listOf(
                        resolveProperty("mod_group"),
                        resolveProperty("mod_subpackage"),
                        project.projectDir.name,
                        "${project.projectDir.name.capitalizeWords()}${resolveProperty("mod_subpackage").capitalizeWords()}${data.name.capitalizeWords()}"
                    ).joinToString(".")
                )
            ),
            "mixins" to listOf<String>(),
            "depends" to dependencies
        )

        val outputDir: File = layout.buildDirectory.asFile.get().resolve("generated/sources/mod_file/${if (name.isEmpty()) "main" else name}")
        val filePath: File = outputDir.resolve("fabric.mod.json")
        Files.createDirectories(filePath.parentFile.toPath())
        FileWriter(filePath, StandardCharsets.UTF_8).use { it.write(JsonOutput.prettyPrint(JsonOutput.toJson(modsJson))) }
        outputs.dir(outputDir)
    }
}

internal val modFile = generateModFile()

client.resources {
    srcDir(modFile)
    source(generated.resources)
    source(main.sourceSets["client"].resources)
    exclude("./cache")
}

tasks.withType<IdeaSyncTask>().configureEach {
    dependsOn(modFile)
}

loom {
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
        }
        named("server") {
            server()
            configName = "Fabric Server"
        }

        configureEach {
            ideConfigGenerated(true)
            source(data)
        }
    }
}

fabricApi.configureDataGeneration {
    client = true
    createRunConfiguration = true
    outputDirectory = generated.resources.srcDirs.first()
}

publication {
    name = "${resolveProperty("mod_name")} (${project.name})"
}
