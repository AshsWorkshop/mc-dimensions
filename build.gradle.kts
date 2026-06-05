import groovy.json.JsonOutput
import io.github.wasabithumb.jtoml.KToml
import io.github.wasabithumb.jtoml.set
import io.github.wasabithumb.jtoml.value.array.TomlArray
import io.github.wasabithumb.jtoml.value.table.TomlTable
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.extra
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.Locale
import net.ashwork.gradle.multiloader.*
import org.slf4j.event.Level

plugins {
    id("multiloader-base")
    id("net.neoforged.moddev")
}
if (!rootProject.extra.has("mod_subpackage")) {
    rootProject.extra["mod_subpackage"] = rootProject.extra["mod_id"]
}

// Create source sets
val common = configureInheritingFeature("common")
val client = configureInheritingFeature("client", "common")
val data = configureInheritingFeature("data", "main", publish = true)

configureInheritingFeature("main", "common", "client", publish = true, bundle = listOf("common", "client"))

internal val generated: SourceSet = sourceSets.create("generated") {
    java.setSrcDirs(emptyList<Any>())
}

enum class VersionPart(val componentIndex: Int) {
    MAJOR(0),
    MINOR(1),
    PATCH(2)
}

fun computeNextVersion(version: String, to: VersionPart = VersionPart.MINOR): String {
    val versionComponents = version.split(".").subList(0, to.componentIndex + 1).toMutableList()
    val toUpdate: Int = versionComponents.removeLast().toInt()
    return "${versionComponents.joinToString(".")}.${toUpdate + 1}"
}

fun generateMixinFile(source: SourceSet, name: String = ""): TaskProvider<Task> = tasks.register("generate${if (name.isEmpty()) "" else name.replaceFirstChar { it.uppercase() }}Mixins") {
    val mixinPath: String = mutableListOf(
        resolveProperty("mod_group").replace(".", File.separator),
        resolveProperty("mod_subpackage")
    ).let {
        if (name.isNotEmpty()) it.add(name)
        it.add("mixin")
        it.joinToString(File.separator)
    }
    val mixins: List<String> = source.allSource.filter {
        it.path.contains(mixinPath) && !it.path.contains("package-info")
    }.map {
        it.path.split("$mixinPath${File.separator}").last().substringBeforeLast('.').replace(File.separator, ".")
    }.toList()

    val mixinsJson: Map<String, Any> = mapOf(
        "required" to true,
        "package" to mixinPath.replace(File.separator, "."),
        "compatibilityLevel" to "JAVA_${resolveProperty("java_version")}",
        (if (source.name == "client") "client" else "mixins") to mixins,
        "injectors" to mapOf(
            "defaultRequire" to 1
        ),
        "mixinextras" to mapOf(
            "minVersion" to "0.5.4"
        )
    )

    val outputDir: File = layout.buildDirectory.asFile.get().resolve("generated/sources/mixins/${source.name}")
    val filePath: File = outputDir.resolve("${resolveProperty("mod_id")}.${source.name}.mixins.json")
    Files.createDirectories(filePath.parentFile.toPath())
    FileWriter(filePath, StandardCharsets.UTF_8).use { it.write(JsonOutput.prettyPrint(JsonOutput.toJson(mixinsJson))) }
    outputs.dir(outputDir)
}

fun generateModFile(name: String = "", dependency: TomlTable? = null, accessTransformers: List<String> = listOf(), enumextensions: Boolean = false, mixins: List<TaskProvider<Task>> = listOf()): TaskProvider<Task> {
    return tasks.register("generate${if (name.isEmpty()) "" else name.replaceFirstChar { it.uppercase() }}ModFile") {
        if (mixins.isNotEmpty()) dependsOn(*mixins.toTypedArray())

        // Base toml
        val modsToml = TomlTable.create()
        modsToml["license"] = resolveProperty("mod_license")

        // Mod entries
        val mods = TomlArray.create()
        modsToml["mods"] = mods

        // Mod
        val modId = "${resolveProperty("mod_id")}${if (name.isEmpty()) "" else "_${name}"}"
        val mod = TomlTable.create()
        mod["modId"] = modId
        mod["version"] = resolveProperty("mod_version")
        mod["displayName"] = resolveProperty("mod_name")
        mod["authors"] = resolveProperty("mod_authors")
        mod["description"] = resolveProperty("mod_description")
        mods.add(mod)

        if (enumextensions) {
            mod["enumExtensions"] = "META-INF/enumextensions.json"
        }

        // Mod dependencies
        val modDependencies = TomlArray.create()
        modsToml["dependencies.${modId}"] = modDependencies

        // Minecraft dependency
        val minecraft = TomlTable.create()
        minecraft["modId"] = "minecraft"
        minecraft["type"] = "required"
        minecraft["versionRange"] = "[${resolveProperty("vanillaMinecraft")},${computeNextVersion(resolveProperty("vanillaMinecraft"))})"
        minecraft["ordering"] = "AFTER"
        minecraft["side"] = "BOTH"
        modDependencies.add(minecraft)

        // NeoForge dependency
        val neoForge = TomlTable.create()
        neoForge["modId"] = "neoforge"
        neoForge["type"] = "required"
        neoForge["versionRange"] = "[${resolveProperty("neoforgeApi")},${computeNextVersion(resolveProperty("neoforgeApi"))})"
        neoForge["ordering"] = "AFTER"
        neoForge["side"] = "BOTH"
        modDependencies.add(neoForge)

        // Additional dependency
        if (dependency != null) {
            modDependencies.add(dependency)
        }

        // Access transformers
        if (accessTransformers.isNotEmpty()) {
            val ats = TomlArray.create()
            modsToml["accessTransformers"] = ats

            accessTransformers.forEach {
                val at = TomlTable.create()
                at["file"] = it
                ats.add(at)
            }
        }

        val mixinConfigs = TomlArray.create()
        modsToml["mixins"] = mixinConfigs
        mixins.flatMap { it.get().outputs.files.asFileTree.files }.forEach {
            val entry = TomlTable.create()
            entry["config"] = it.path.substring(it.path.lastIndexOf(File.separator) + 1)
            mixinConfigs.add(entry)
        }

        // Write to file
        val outputDir: File = layout.buildDirectory.asFile.get().resolve("generated/sources/mod_file/${if (name.isEmpty()) "main" else name}")
        val filePath: File = outputDir.resolve("META-INF/neoforge.mods.toml")
        Files.createDirectories(filePath.parentFile.toPath())
        FileWriter(filePath, StandardCharsets.UTF_8).use { KToml.write(it, modsToml) }
        outputs.dir(outputDir)
    }
}

val transformers = sourceSets.create("transformers") {
    java.setSrcDirs(listOf<Any>())
}
val localResources = sourceSets.create("local") {
    java.setSrcDirs(listOf<Any>())
}

internal val commonMixins = generateMixinFile(common)
internal val clientMixins = generateMixinFile(client, "client")
internal val dataMixins = generateMixinFile(data, "data")
internal val modFile = generateModFile(accessTransformers = transformers.resources.map {
    it.toRelativeString(transformers.resources.srcDirs.first()).replace(File.separator, "/")
}, mixins = listOf(commonMixins, clientMixins, dataMixins), enumextensions = true)

client.resources {
    srcDir(modFile)
    source(generated.resources)
    source(transformers.resources)
    srcDir(commonMixins)
    srcDir(clientMixins)
    srcDir(dataMixins)
    exclude("./cache")
}

neoForge {
    version = resolveProperty("neoforgeApi")

    // Sync tasks
    ideSyncTask(modFile)
    ideSyncTask(commonMixins)
    ideSyncTask(clientMixins)
    ideSyncTask(dataMixins)

    addModdingDependenciesTo(common)

    transformers.resources.forEach { accessTransformers.from(it) }
    interfaceInjectionData.from(*localResources.resources.filter { it.path.endsWith("interfaces.json") }.files.toTypedArray())

    mods.create(resolveProperty("mod_id")) {
        sourceSets.forEach {
            sourceSet(it)
        }
    }

    runs {
        create("clientSlim") {
            client()
        }
        create("clientDefault") {
            client()
            programArguments.addAll("--username", "Steve")
        }
        create("server") {
            server()
            programArgument("--nogui")
        }
        create("gameTestServer") {
            type = "gameTestServer"
        }
        create("clientData") {
            clientData()
            programArguments.addAll("--mod", resolveProperty("mod_id"), "--all", "--output", generated.resources.srcDirs.first().absolutePath)
            client.resources.srcDirs.forEach { programArguments.addAll("--existing", it.absolutePath) }
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = Level.DEBUG
        }
        named { !it.lowercase(Locale.ROOT).contains("data") }.configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", resolveProperty("mod_id"))
        }
    }
}

publication {
    name = "${resolveProperty("mod_name")} (${project.name})"
}
