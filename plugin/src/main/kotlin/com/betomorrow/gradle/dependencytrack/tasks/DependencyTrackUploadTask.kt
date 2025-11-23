package com.betomorrow.gradle.dependencytrack.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class DependencyTrackUploadTask : DefaultTask() {
    @TaskAction
    fun upload() {
        logger.info("Uploading SBOM to Dependency-Track...")
    }
}
