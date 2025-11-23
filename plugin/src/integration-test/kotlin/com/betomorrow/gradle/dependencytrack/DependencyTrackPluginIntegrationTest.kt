package com.betomorrow.gradle.dependencytrack

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals

internal class DependencyTrackPluginIntegrationTest {
    @field:TempDir
    private lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }

    @BeforeEach
    fun setup() {
        settingsFile.writeText("")
    }

    @Test
    fun `project can run upload task`() {
        buildFile.writeText(
            """
            plugins {
                id('com.betomorrow.dependency-track')
            }
            """.trimIndent(),
        )

        val runner =
            GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments("uploadToDependencyTrack", "--info", "--stacktrace")
                .withProjectDir(projectDir)

        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":uploadToDependencyTrack")?.outcome)
    }
}
