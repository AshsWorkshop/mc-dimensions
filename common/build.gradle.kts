import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate
import net.ashwork.gradle.multiloader.*
import java.util.Locale

plugins {
    id("multiloader-base")
    id("net.neoforged.moddev")
}

// Create source sets
internal val common: SourceSet = sourceSets.createFrom("common", sourceSets["main"])
internal val client: SourceSet = sourceSets.createFrom("client", common, common)
internal val data: SourceSet = sourceSets.createFrom("data", client, client)

dependencies {
    implementation("net.ashwork.mc:ashsmultiloader-api")
    implementation("net.ashwork.mc:ashsmultiloader-api-data")
    implementation(group = "net.ashwork.mc", name = "ashsmultiloader-api-data", classifier = "accesstransformer", ext = "cfg")
}

neoForge {
    // Configure vanilla mode
    neoFormVersion = resolveProperty("vanillaNeoform")

    accessTransformers.from(configurations.runtimeClasspath.get().files.filter { it.path.endsWith("cfg") })
}

afterEvaluate {
    publishSourceSets(
        project.name, listOf(common, client),
        project.base.archivesName.get()
    )
}
