package com.betomorrow.gradle.dependencytrack.api

import org.gradle.api.logging.Logging
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Base64

class DependencyTrackRequestFailedException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class DependencyTrackApiClient(
    private val host: URI,
    private val apiKey: String,
) {
    private val client = HttpClient.newHttpClient()

    init {
        require(host.isAbsolute) { "The host must be an absolute URI" }
        require(apiKey.isNotBlank()) { "The API key must not be blank" }
    }

    fun createGroup(groupName: String): HttpResponse<String> {
        val jsonPayload =
            """
            {
              "name": "$groupName",
              "classifier": "APPLICATION",
              "active": true,
              "tags": [],
              "collectionLogic": "AGGREGATE_DIRECT_CHILDREN"
            }
            """.trimIndent()

        val request =
            HttpRequest.newBuilder()
                .uri(host.resolve("/api/v1/project"))
                .header("Content-Type", "application/json")
                .header("X-API-Key", apiKey)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build()

        return sendRequest(
            request,
            onSuccess = { logger.info("Group created successfully in Dependency-Track.") },
            onFailure = {
                DependencyTrackRequestFailedException("Failed to create group in Dependency-Track. Status: ${it.statusCode()} - Body: ${it.body()}")
            },
        )
    }

    fun uploadSbom(
        projectName: String,
        projectVersion: String,
        groupName: String?,
        sbomFile: File,
    ): HttpResponse<String> {
        val base64Sbom = Base64.getEncoder().encodeToString(sbomFile.readBytes())
        val jsonPayload =
            """
            {
              "projectName": "$projectName",
              "projectVersion": "$projectVersion",
              "autoCreate": true,
              ${if (groupName != null) "\"parentName\": \"$groupName\"," else ""}
              "bom": "$base64Sbom"
            }
            """.trimIndent()
        return uploadSbom(jsonPayload)
    }

    fun uploadSbom(
        projectId: String,
        projectVersion: String,
        sbomFile: File,
    ): HttpResponse<String> {
        val base64Sbom = Base64.getEncoder().encodeToString(sbomFile.readBytes())
        val jsonPayload =
            """
            {
              "project": "$projectId",
              "projectVersion": "$projectVersion",
              "bom": "$base64Sbom"
            }
            """.trimIndent()
        return uploadSbom(jsonPayload)
    }

    private fun uploadSbom(jsonPayload: String): HttpResponse<String> {
        val request =
            HttpRequest.newBuilder()
                .uri(host.resolve("/api/v1/bom"))
                .header("Content-Type", "application/json")
                .header("X-API-Key", apiKey)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build()

        return sendRequest(
            request,
            onSuccess = { logger.info("SBOM uploaded successfully to Dependency-Track.") },
            onFailure = {
                DependencyTrackRequestFailedException("Failed to upload SBOM to Dependency-Track. Status: ${it.statusCode()} - Body: ${it.body()}")
            },
        )
    }

    private fun sendRequest(
        request: HttpRequest,
        onSuccess: ((HttpResponse<String>) -> Unit)?,
        onFailure: ((HttpResponse<String>) -> Exception)?,
    ): HttpResponse<String> {
        val response =
            try {
                client.send(request, HttpResponse.BodyHandlers.ofString())
            } catch (e: Exception) {
                throw DependencyTrackRequestFailedException("Failed to send request to Dependency-Track.", e)
            }
        if (response.statusCode() in 200..299) {
            onSuccess?.invoke(response)
            logger.debug("Response: {}", response.body())
        } else {
            if (onFailure != null) {
                throw onFailure.invoke(response)
            } else {
                throw DependencyTrackRequestFailedException(
                    "Failed to send request to Dependency-Track. Status: ${response.statusCode()} - Body: ${response.body()}",
                )
            }
        }
        return response
    }

    companion object {
        private val logger = Logging.getLogger(DependencyTrackApiClient::class.java)
    }
}
