package com.betomorrow.gradle.jacoco

data class CoverageCounter(
    val type: String,
    val covered: Double,
    val missed: Double,
)
