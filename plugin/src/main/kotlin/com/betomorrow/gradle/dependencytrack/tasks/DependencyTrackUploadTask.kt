package com.betomorrow.gradle.dependencytrack.tasks

import com.betomorrow.gradle.dependencytrack.api.ApiLogLevel
import com.betomorrow.gradle.dependencytrack.api.DependencyTrackClient
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectName
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectVersion
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.net.URI
import java.util.UUID

abstract class DependencyTrackUploadTask : DefaultTask() {
    @get:Input
    abstract val apiHost: Property<URI>

    @get:Input
    abstract val apiKey: Property<String>

    @get:Input
    @get:Optional
    abstract val apiLogLevel: Property<ApiLogLevel>

    @get:Input
    @get:Optional
    abstract val projectId: Property<UUID>

    @get:Input
    @get:Optional
    abstract val projectName: Property<String>

    private val dTrackProjectName: Provider<ProjectName> = projectName.map { ProjectName(it) }

    @get:Input
    abstract val projectVersion: Property<String>

    private val dTrackProjectVersion: Provider<ProjectVersion> = projectVersion.map { ProjectVersion(it) }

    @get:Input
    @get:Optional
    abstract val groupName: Property<String>

    private val dTrackGroupName: Provider<ProjectName> = groupName.map { ProjectName(it) }

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

        val dependencyTrackClient = DependencyTrackClient(apiHost.get(), apiKey.get(), apiLogLevel.get())

        when {
            projectId.isPresent -> dependencyTrackClient.uploadSbom(projectId.get(), dTrackProjectVersion.get(), sbomFile.get().asFile)
            dTrackProjectName.isPresent -> {
                if (dTrackGroupName.isPresent) {
                    logger.info("Creating group {}...", dTrackGroupName.get())
                    dependencyTrackClient.createGroup(dTrackGroupName.get())
                }
                dependencyTrackClient.uploadSbom(
                    dTrackProjectName.get(),
                    dTrackProjectVersion.get(),
                    dTrackGroupName.orNull,
                    sbomFile.get().asFile,
                )
            }

            else -> throw IllegalArgumentException("Either project ID or project name must be specified")
        }
    }

    private fun logParameters() {
        if (logger.isInfoEnabled) {
            logger.info("DependencyTrack Upload: Parameters")
            logger.info("------------------------------------------------------------------------")
            logger.info("  apiHost        : {}", apiHost.get())
            logger.info("  apiKey         : {}", apiKey.get())
            logger.info("  apiLogLevel    : {}", apiLogLevel.get())
            logger.info("  projectId      : {}", projectId.orNull)
            logger.info("  projectName    : {}", projectName.get())
            logger.info("  projectVersion : {}", projectVersion.get())
            logger.info("  groupName      : {}", groupName.orNull)
            logger.info("  sbomFile       : {}", sbomFile.orNull)
            logger.info("------------------------------------------------------------------------")
        }
    }
}
