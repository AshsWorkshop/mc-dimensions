// Add gradle properties
apply(from = "properties.gradle.kts")

if (!rootProject.extra.has("mod_subpackage")) {
    rootProject.extra["mod_subpackage"] = rootProject.extra["mod_id"]
}
