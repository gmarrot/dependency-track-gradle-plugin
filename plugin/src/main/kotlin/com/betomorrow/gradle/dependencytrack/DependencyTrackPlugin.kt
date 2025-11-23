package com.betomorrow.gradle.dependencytrack

import com.betomorrow.gradle.dependencytrack.tasks.DependencyTrackUploadTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.net.URI
import java.util.UUID

class DependencyTrackPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extensions = project.extensions.create("dependencyTrack", DependencyTrackExtensions::class.java, project)

        project.tasks.register("uploadToDependencyTrack", DependencyTrackUploadTask::class.java) { task ->
            task.apiHost.set(extensions.apiHost.map { URI.create(it) })
            task.apiKey.set(extensions.apiKey)
            task.apiLogLevel.set(extensions.apiLogLevel)
            task.projectId.set(extensions.projectId.map { UUID.fromString(it) })
            task.projectName.set(extensions.projectName)
            task.projectVersion.set(extensions.projectVersion)

            task.groupName.set(extensions.groupName)

            task.sbomFile.set(extensions.sbomFile)
        }
    }
}
