plugins {
    // https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

val modId: String = providers.gradleProperty("mod_id").get()

rootProject.name = modId

// Include all projects
rootProject.projectDir.listFiles {
    it.isDirectory
            && it.name != "buildSrc"
            && (it.resolve("build.gradle").exists() || it.resolve("build.gradle.kts").exists())
}.forEach {
    // This allows use of project name as an authoritative module component name.
    // This is useful for a number of reasons; among others, it lets us reason about feature capability names at
    // configuration time (see ProjectExtensions.publishedAccessTransformer).
    //
    // See https://lukebemish.dev/2026/02/22/gradle-shorts-1-publication-coordinates.html for more details about why
    // this is probably the preferable way to do things instead of configuring on the publication.
    val projectName = ":${modId}-${it.toRelativeString(rootProject.projectDir)}"
    include(projectName)
    project(projectName).projectDir = it
}
