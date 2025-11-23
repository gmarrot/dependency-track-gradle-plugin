package com.betomorrow.gradle.dependencytrack.tasks

import com.betomorrow.gradle.dependencytrack.api.DependencyTrackApiClient
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.net.URI

abstract class DependencyTrackUploadTask : DefaultTask() {
    @get:Input
    abstract val apiHost: Property<URI>

    @get:Input
    abstract val apiKey: Property<String>

    @get:Input
    @get:Optional
    abstract val projectId: Property<String>

    @get:Input
    abstract val projectName: Property<String>

    @get:Input
    abstract val projectVersion: Property<String>

    @get:Input
    @get:Optional
    abstract val groupName: Property<String>

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val sbomFile: RegularFileProperty

    init {
        group = "reporting"
        description = "Uploads the built Software Bill of Materials to Dependency-Track."
    }

    @TaskAction
    fun upload() {
        logParameters()
        logger.info("Uploading SBOM to Dependency-Track...")

        val apiClient = DependencyTrackApiClient(apiHost.get(), apiKey.get())

        val response =
            when {
                projectId.isPresent -> apiClient.uploadSbom(projectId.get(), projectVersion.get(), sbomFile.get().asFile)
                projectName.isPresent -> {
                    if (groupName.isPresent) {
                        apiClient.createGroup(groupName.get())
                    }
                    apiClient.uploadSbom(projectName.get(), projectVersion.get(), groupName.orNull, sbomFile.get().asFile)
                }

                else -> throw IllegalArgumentException("Either project ID or project name must be specified")
            }
    }

    private fun logParameters() {
        if (logger.isInfoEnabled) {
            logger.info("DependencyTrack Upload: Parameters")
            logger.info("------------------------------------------------------------------------")
            logger.info("  apiHost: {}", apiHost.get())
            logger.info("  apiKey: {}", apiKey.get())
            logger.info("  projectId: {}", projectId.orNull)
            logger.info("  projectName: {}", projectName.get())
            logger.info("  projectVersion: {}", projectVersion.get())
            logger.info("  groupName: {}", groupName.orNull)
            logger.info("  sbomFile: {}", sbomFile.orNull)
            logger.info("------------------------------------------------------------------------")
        }
    }
}
