package com.betomorrow.gradle.dependencytrack.api

import com.betomorrow.gradle.dependencytrack.api.dtos.BomSubmitRequest
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectClassifier
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectCollectionLogic
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectCreationInput
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectName
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectVersion
import com.betomorrow.gradle.dependencytrack.infra.feign.GradleFeignLogger
import feign.Feign
import feign.FeignException
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import org.gradle.api.logging.Logging
import java.io.File
import java.net.URI
import java.util.Base64
import java.util.UUID

class DependencyTrackClient(
    host: URI,
    apiKey: String,
    logLevel: ApiLogLevel,
) {
    private val client: DependencyTrackApi

    init {
        require(host.isAbsolute) { "The host must be an absolute URI" }
        require(apiKey.isNotBlank()) { "The API key must not be blank" }

        client =
            Feign
                .builder()
                .encoder(GsonEncoder())
                .decoder(GsonDecoder())
                .requestInterceptor { requestTemplate ->
                    requestTemplate
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", apiKey)
                }
                .logger(GradleFeignLogger(DependencyTrackApi::class.java))
                .logLevel(logLevel.toFeignLogLevel())
                .target(DependencyTrackApi::class.java, host.toString())
    }

    fun createGroup(groupName: ProjectName) {
        val request =
            ProjectCreationInput(
                name = groupName,
                classifier = ProjectClassifier.APPLICATION,
                collectionLogic = ProjectCollectionLogic.AGGREGATE_DIRECT_CHILDREN,
                active = true,
            )
        try {
            val group = client.createProject(request)
            logger.info("Group {} created in Dependency-Track (UUID: {}).", groupName, group.uuid)
        } catch (e: FeignException) {
            if (e.status() == 409) {
                logger.info("Group {} already exists in Dependency-Track.", groupName)
                return
            }
            throw e
        }
    }

    fun uploadSbom(
        projectId: UUID,
        projectVersion: ProjectVersion,
        sbomFile: File,
    ) {
        val base64Sbom = Base64.getEncoder().encodeToString(sbomFile.readBytes())
        val request =
            BomSubmitRequest(
                project = projectId,
                projectVersion = projectVersion,
                bom = base64Sbom,
            )
        return uploadSbom(request)
    }

    fun uploadSbom(
        projectName: ProjectName,
        projectVersion: ProjectVersion,
        groupName: ProjectName?,
        sbomFile: File,
    ) {
        val base64Sbom = Base64.getEncoder().encodeToString(sbomFile.readBytes())
        val request =
            BomSubmitRequest(
                projectName = projectName,
                projectVersion = projectVersion,
                autoCreate = true,
                parentName = groupName,
                bom = base64Sbom,
            )
        return uploadSbom(request)
    }

    private fun uploadSbom(request: BomSubmitRequest) {
        val response = client.submitBom(request)
        logger.info("SBOM uploaded to Dependency-Track (Token: {}).", response.token)
    }

    companion object {
        private val logger = Logging.getLogger(DependencyTrackClient::class.java)
    }
}
