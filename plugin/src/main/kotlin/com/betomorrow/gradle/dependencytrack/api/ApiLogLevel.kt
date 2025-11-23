package com.betomorrow.gradle.dependencytrack.api

enum class ApiLogLevel {
    /** No logging. */
    NONE,

    /** Log only the request method and URL and the response status code and execution time. */
    BASIC,

    /** Log the basic information along with request and response headers. */
    HEADERS,

    /** Log the headers, body, and metadata for both requests and responses. */
    FULL,
}

fun ApiLogLevel.toFeignLogLevel(): feign.Logger.Level =
    when (this) {
        ApiLogLevel.NONE -> feign.Logger.Level.NONE
        ApiLogLevel.BASIC -> feign.Logger.Level.BASIC
        ApiLogLevel.HEADERS -> feign.Logger.Level.HEADERS
        ApiLogLevel.FULL -> feign.Logger.Level.FULL
    }
