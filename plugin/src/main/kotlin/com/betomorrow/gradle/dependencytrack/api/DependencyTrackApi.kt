package com.betomorrow.gradle.dependencytrack.api

import com.betomorrow.gradle.dependencytrack.api.dtos.BomSubmitRequest
import com.betomorrow.gradle.dependencytrack.api.dtos.BomUploadResponse
import com.betomorrow.gradle.dependencytrack.api.dtos.Project
import com.betomorrow.gradle.dependencytrack.api.dtos.ProjectCreationInput
import feign.RequestLine

interface DependencyTrackApi {
    @RequestLine("PUT /api/v1/bom")
    fun submitBom(input: BomSubmitRequest): BomUploadResponse

    @RequestLine("PUT /api/v1/project")
    fun createProject(input: ProjectCreationInput): Project
}
