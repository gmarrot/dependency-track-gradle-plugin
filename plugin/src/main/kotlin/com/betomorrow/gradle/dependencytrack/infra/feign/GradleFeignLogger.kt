package com.betomorrow.gradle.dependencytrack.infra.feign

import feign.Request
import feign.Response
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class GradleFeignLogger(
    private val logger: Logger,
) : feign.Logger() {
    constructor() : this(Logging.getLogger(feign.Logger::class.java))

    constructor(clazz: Class<*>) : this(Logging.getLogger(clazz))

    constructor(name: String) : this(Logging.getLogger(name))

    override fun logRequest(configKey: String?, logLevel: Level?, request: Request?) {
        if (logger.isDebugEnabled) {
            super.logRequest(configKey, logLevel, request)
        }
    }

    override fun logAndRebufferResponse(configKey: String?, logLevel: Level?, response: Response?, elapsedTime: Long): Response? =
        if (logger.isDebugEnabled) {
            super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime)
        } else {
            response
        }

    override fun log(configKey: String?, format: String?, vararg args: Any?) {
        if (logger.isDebugEnabled) {
            logger.debug(String.format(methodTag(configKey) + format, *args))
        }
    }
}
