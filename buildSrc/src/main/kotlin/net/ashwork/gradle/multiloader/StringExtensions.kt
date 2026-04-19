package net.ashwork.gradle.multiloader

import java.util.Locale

fun String.capitalizeWords(): String = this.split(" ").map {
    it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}.joinToString(" ")
