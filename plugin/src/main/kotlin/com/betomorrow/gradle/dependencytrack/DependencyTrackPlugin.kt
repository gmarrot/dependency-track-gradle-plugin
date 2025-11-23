package com.betomorrow.gradle.dependencytrack

import com.betomorrow.gradle.dependencytrack.tasks.DependencyTrackUploadTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyTrackPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("uploadToDependencyTrack", DependencyTrackUploadTask::class.java)
    }
}
