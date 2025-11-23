package com.betomorrow.gradle.dependencytrack

import com.betomorrow.gradle.dependencytrack.api.ApiLogLevel
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

abstract class DependencyTrackExtensions(
    project: Project,
) {
    abstract val apiHost: Property<String>
    abstract val apiKey: Property<String>
    abstract val apiLogLevel: Property<ApiLogLevel>

    abstract val projectId: Property<String>
    abstract val projectName: Property<String>
    abstract val projectVersion: Property<String>

    abstract val groupName: Property<String>

    abstract val sbomFile: RegularFileProperty

    init {
        apiLogLevel.convention(ApiLogLevel.BASIC)
        projectName.convention(project.name)
        when (val version = project.version.toString()) {
            "unspecified" -> projectVersion.convention("latest")
            else -> projectVersion.convention(version)
        }
    }
}
