package com.betomorrow.gradle.dependencytrack.api.dtos

import java.util.UUID

data class BomSubmitRequest(
    val project: UUID? = null,
    val projectName: ProjectName? = null,
    val projectVersion: ProjectVersion,
    val autoCreate: Boolean? = null,
    val parentName: ProjectName? = null,
    val bom: String,
) {
    init {
        require(project != null || projectName != null) { "Either project or projectName must be provided" }
    }
}

data class BomUploadResponse(
    val token: String,
)
