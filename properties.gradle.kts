// Gradle libraries
// - https://repo.maven.apache.org/maven2/io/github/wasabithumb/jtoml/
val gradleJtoml by extra("1.5.2")
// - https://plugins.gradle.org/plugin/de.undercouch.download
val gradleDownload by extra("5.7.0")

// Vanilla
val vanillaMinecraft by extra("26.1.2")
// - https://projects.neoforged.net/neoforged/neoform
val vanillaNeoform by extra("${vanillaMinecraft}-1")

// NeoForge
// - https://projects.neoforged.net/neoforged/moddevgradle
val neoforgeGradle by extra("2.0.141")
// - https://projects.neoforged.net/neoforged/neoforge
val neoforgeApi by extra("${vanillaMinecraft}.7-beta")

// Fabric
// - https://fabricmc.net/develop/
val fabricLoader by extra("0.19.1")
val fabricLoom by extra("1.16-SNAPSHOT")
val fabricApi by extra("0.145.4+${vanillaMinecraft}")
