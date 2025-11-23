package com.betomorrow.gradle.dependencytrack.api.dtos

data class Tag(
    val name: String,
) {
    init {
        require(name.isNotBlank()) { "Tag name cannot be blank" }
    }
}
