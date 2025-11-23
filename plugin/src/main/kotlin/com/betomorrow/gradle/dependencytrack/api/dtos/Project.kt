package com.betomorrow.gradle.dependencytrack.api.dtos

import java.util.UUID

data class Project(
    val uuid: UUID,
    val name: ProjectName,
    val version: ProjectVersion?,
    val classifier: ProjectClassifier,
    val collectionLogic: ProjectCollectionLogic,
    val tags: List<Tag>,
    val active: Boolean,
)

data class ProjectCreationInput(
    val name: ProjectName,
    val version: ProjectVersion? = null,
    val classifier: ProjectClassifier,
    val collectionLogic: ProjectCollectionLogic,
    val tags: List<Tag>? = null,
    val active: Boolean,
)

@JvmInline
value class ProjectName(
    private val value: String,
) {
    init {
        require(value.isNotBlank()) { "Project name cannot be blank" }
        require(REGEX.matches(value)) { "Project name must match $REGEX" }
    }

    companion object {
        private val REGEX = "^[\\s\\p{L}\\p{M}\\p{S}\\p{N}\\p{P}]*$".toRegex()
    }
}

@JvmInline
value class ProjectVersion(
    private val value: String,
) {
    init {
        require(value.isNotBlank()) { "Project version cannot be blank" }
        require(REGEX.matches(value)) { "Project version must match $REGEX" }
    }

    companion object {
        private val REGEX = "^[\\s\\p{L}\\p{M}\\p{S}\\p{N}\\p{P}]*$".toRegex()
    }
}

enum class ProjectClassifier {
    NONE,
    APPLICATION,
    FRAMEWORK,
    LIBRARY,
    CONTAINER,
    OPERATING_SYSTEM,
    DEVICE,
    FIRMWARE,
    FILE,
    PLATFORM,
    DEVICE_DRIVER,
    MACHINE_LEARNING_MODEL,
    DATA,
}

enum class ProjectCollectionLogic {
    NONE,
    AGGREGATE_DIRECT_CHILDREN,
    AGGREGATE_DIRECT_CHILDREN_WITH_TAG,
    AGGREGATE_LATEST_VERSION_CHILDREN,
}
