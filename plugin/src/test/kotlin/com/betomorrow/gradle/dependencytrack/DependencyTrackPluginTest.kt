package com.betomorrow.gradle.dependencytrack

import com.betomorrow.gradle.dependencytrack.tasks.DependencyTrackUploadTask
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

internal class DependencyTrackPluginTest {
    @Test
    fun `plugin registers upload task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.betomorrow.dependency-track")

        val uploadTask = project.tasks.findByName("uploadToDependencyTrack")
        assertNotNull(uploadTask) { "Upload task should be registered" }
        assert(uploadTask is DependencyTrackUploadTask) { "Upload task should be of correct type" }
    }
}
