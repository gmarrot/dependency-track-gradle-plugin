import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions")
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    gradleReleaseChannel = "current"

    rejectVersionIf {
        // Disallow release candidates as upgradable versions from stable versions
        !candidate.version.isStable() && currentVersion.isStable()
    }
}

fun String.isStable(): Boolean {
    val normalizedVersion = this.uppercase()

    val stableKeyword = setOf("RELEASE", "FINAL", "GA").any { keyword -> keyword in normalizedVersion }
    val regex = "^[\\d,.v-]+(-r)?$".toRegex()

    return stableKeyword || regex.matches(this)
}
