plugins {
    // https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "dimensions"

// Include all projects
rootProject.projectDir.listFiles {
    it.isDirectory
            && it.name != "buildSrc"
            && (it.resolve("build.gradle").exists() || it.resolve("build.gradle.kts").exists())
}.forEach {
    include(it.toRelativeString(rootProject.projectDir))
}
