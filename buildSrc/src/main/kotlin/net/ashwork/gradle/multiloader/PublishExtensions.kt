package net.ashwork.gradle.multiloader

import groovy.namespace.QName
import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.XmlProvider
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import kotlin.text.set

fun Project.publishSourceSets(name: String, sources: List<SourceSet>, baseName: String, withSources: Boolean = true): TaskProvider<Jar> {
    return publishSourceSets(name, sources, baseName, withSources) {}
}

fun Project.publishSourceSets(name: String, sources: List<SourceSet>, baseName: String, withSources: Boolean = true, configurePom: Action<MavenPom>): TaskProvider<Jar> {
    fun addLicense(copy: AbstractCopyTask) {
        var license = listOf(project, rootProject).asSequence().filter { it.file("LICENSE").exists() }.map { it.file("LICENSE") }.firstOrNull()
        if (license != null) {
            copy.from(license) {
                rename { "META-INF/${it}"}
            }
        }
    }

    val artifacts: MutableList<TaskProvider<Jar>> = mutableListOf(
        tasks.register<Jar>("${name}Jar") {
            group = project.group as String
            archiveBaseName.set(baseName)
            from(*(sources.map { it.output }.toTypedArray()))
            addLicense(this)
        }
    )

    if (withSources) {
        artifacts.add(
            tasks.register<Jar>("${name}JarSources") {
                group = project.group as String
                archiveBaseName.set(baseName)
                archiveClassifier.set("sources")
                from(*(sources.map { it.allSource }.toTypedArray()))
                addLicense(this)
            }
        )
    }

    extensions.configure<PublishingExtension>("publishing") {
        publications.create<MavenPublication>(name) {
            artifactId = baseName
            setArtifacts(artifacts)

            pom(configurePom)
        }
    }

    return artifacts[0]
}

fun interface DependencyProvider {

    fun dependencyList(): Node

    fun compile(configuration: NamedDomainObjectProvider<Configuration>, matches: (String) -> Boolean) {
        dependency(configuration, "compile", matches)
    }

    fun compile(artifact: TaskProvider<Jar>) {
        dependency(artifact, "compile")
    }

    fun runtime(configuration: NamedDomainObjectProvider<Configuration>, matches: (String) -> Boolean) {
        dependency(configuration, "runtime", matches)
    }

    fun runtime(artifact: TaskProvider<Jar>) {
        dependency(artifact, "runtime")
    }

    fun dependency(configuration: NamedDomainObjectProvider<Configuration>, scope: String, matches: (String) -> Boolean) {
        dependency(configuration.get(), scope, matches)
    }

    fun dependency(configuration: Configuration, scope: String, matches: (String) -> Boolean) {
        configuration.resolvedConfiguration.resolvedArtifacts.filter { matches(it.name) }.forEach {
            dependency(it, scope)
        }
    }

    fun dependency(artifact: ResolvedArtifact, scope: String) {
        dependency(artifact.moduleVersion.id.group, artifact.name, artifact.moduleVersion.id.version, scope)
    }

    fun dependency(artifact: TaskProvider<Jar>, scope: String) {
        val jar = artifact.get()
        dependency(jar.group!!, jar.archiveBaseName.get(), jar.archiveVersion.get(), scope)
    }

    fun dependency(groupId: String, artifactId: String, version: String, scope: String? = null) {
        val dependency = this.dependencyList().appendNode("dependency")
        dependency.appendNode("groupId", groupId)
        dependency.appendNode("artifactId", artifactId)
        dependency.appendNode("version", version)
        if (scope != null) dependency.appendNode("scope", scope)
    }
}

fun MavenPom.dependencies(configure: Action<DependencyProvider>) {
    withXml {
        configure.execute { asNode().getOrCreate("dependencies") }
    }
}

fun MavenPom.dependencyManagement(configure: Action<DependencyProvider>) {
    withXml {
        configure.execute { asNode().getOrCreate("dependencyManagement").getOrCreate("dependencies") }
    }
}

fun Node.getOrCreate(name: String): Node = if (this.getAt(QName(name)).isNotEmpty()) this.getAt(QName(name))[0] as Node else this.appendNode(name)
