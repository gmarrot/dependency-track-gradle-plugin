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
    private val sbomFile by lazy { projectDir.resolve("bom.json") }

    @BeforeEach
    fun setup() {
        settingsFile.writeText("")
        sbomFile.writeText("")
    }

    @Test
    fun `project can run upload task`() {
        buildFile.writeText(
            """
            plugins {
                id('com.betomorrow.dependency-track')
            }

            dependencyTrack {
                apiHost = 'http://localhost:8081'
                apiKey = 'API_KEY'

                sbomFile = file("bom.json")
            }
            """.trimIndent(),
        )

        val runner =
            GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withArguments("uploadToDependencyTrack", "--info", "--stacktrace")
                .withProjectDir(projectDir)

        val firstRunResult = runner.build()

        assertEquals(TaskOutcome.SUCCESS, firstRunResult.task(":uploadToDependencyTrack")?.outcome)

        val secondRunResult = runner.build()

        assertEquals(TaskOutcome.UP_TO_DATE, secondRunResult.task(":uploadToDependencyTrack")?.outcome)
    }
}
