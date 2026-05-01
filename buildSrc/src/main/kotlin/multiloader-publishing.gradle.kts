import groovy.json.JsonSlurper
import net.ashwork.gradle.multiloader.*
import org.gradle.api.Action
import org.gradle.api.publish.maven.MavenPomLicense
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.withType

plugins {
    id("de.undercouch.download")
    `maven-publish`
}

group = resolveProperty("mod_group")

download.run {
    src("https://raw.githubusercontent.com/spdx/license-list-data/main/json/details/${resolveProperty("mod_license")}.json")
    dest(rootProject.layout.buildDirectory.asFile.get().resolve("licenses/${resolveProperty("mod_license")}.json"))
    onlyIfModified(true)
}

fun fromLicenseShortcode(shortcode: String): Action<MavenPomLicense> {
    val license: Map<String, Any> = JsonSlurper().parse(rootProject.layout.buildDirectory.asFile.get().resolve("licenses/${shortcode}.json")) as Map<String, Any>
    return object : Action<MavenPomLicense> {
        override fun execute(l: MavenPomLicense) {
            l.name = license["licenseId"] as String
            l.url = (license["seeAlso"] as List<String>)[0]
            if ("licenseComments" in license) {
                l.comments = license["licenseComments"] as String
            }
            l.distribution = "repo"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHub"
            url = uri("https://maven.pkg.${resolveProperty("mod_repository")}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications.withType<MavenPublication>().configureEach {
        pom {
            name = "${resolveProperty("mod_name")} (${project.name})"
            description = resolveProperty("mod_description")
            url = "https://${resolveProperty("mod_repository")}"
            licenses {
                license(fromLicenseShortcode(resolveProperty("mod_license")))
            }
            issueManagement {
                url = "https://${resolveProperty("mod_repository")}/issues"
                system = resolveProperty("mod_issue_system")
            }
            developers {
                resolveProperty("mod_authors").split(",").forEach {
                    developer {
                        name = it.trim()
                        organization = resolveProperty("mod_organization")
                    }
                }
            }
            scm {
                connection = "scm:git:git://${resolveProperty("mod_repository")}.git"
                developerConnection = "scm:git:ssh://${resolveProperty("mod_repository")}.git"
                url = "https://${resolveProperty("mod_repository")}"
                try {
                    tag = "git rev-parse --verify HEAD".runCommand(workingDir = rootProject.rootDir)
                } catch (e: Exception) {
                    project.logger.warn("Unable to set tag for scm in artifact pom: ${e.message}")
                }
            }
        }
    }
}