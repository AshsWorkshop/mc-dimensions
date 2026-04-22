import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate
import net.ashwork.gradle.multiloader.*
import java.util.Locale

plugins {
    id("multiloader-base")
    id("net.neoforged.moddev")
}

// Create source sets
val common = configureInheritingFeature("common")
val client = configureInheritingFeature("client", "common")
val data = configureInheritingFeature("data", "main", publish = true)

configureInheritingFeature("main", "common", "client", publish = true, bundle = listOf("common", "client"))

neoForge {
    // Configure vanilla mode
    neoFormVersion = resolveProperty("vanillaNeoform")

    addModdingDependenciesTo(common)
}

val commonImplementation by configurations.getting

dependencies {
    commonImplementation(platform("net.ashwork.mc:ashsmultiloader:${resolveProperty("vanillaMinecraft")}.+"))
    commonImplementation("net.ashwork.mc:ashsmultiloader-api") {
        capabilities {
            requireFeature("data")
        }
    }
    accessTransformers(platform("net.ashwork.mc:ashsmultiloader:${resolveProperty("vanillaMinecraft")}.+"))
    accessTransformers("net.ashwork.mc:ashsmultiloader-api")
    accessTransformers("net.ashwork.mc:ashsmultiloader-api") {
        capabilities {
            requireFeature("data")
        }
    }
}

publication {
    name = "${resolveProperty("mod_name")} (${project.name})"
}
