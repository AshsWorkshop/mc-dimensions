package net.ashwork.gradle.multiloader

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.extra

fun Project.resolveProperty(name: String): String {
    if (project.extra.has(name)) return project.extra[name].toString()
    return rootProject.extra[name].toString()
}

fun SourceSetContainer.createFrom(name: String, withClasspath: SourceSet, vararg withOutputs: SourceSet): SourceSet {
    return this.create(name) {
        compileClasspath += withClasspath.compileClasspath
        runtimeClasspath += withClasspath.runtimeClasspath
        withOutputs.forEach {
            compileClasspath += it.output
            runtimeClasspath += it.output
        }
    }
}
