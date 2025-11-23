import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

val jvmTarget: String by project
val junitVersion: String? by project
val ktlintVersion: String? by project

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(jvmTarget)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

if (ktlintVersion != null) {
    ktlint {
        version = ktlintVersion
    }
}

testing {
    suites {
        withType<JvmTestSuite> {
            if (junitVersion != null) {
                useJUnitJupiter(junitVersion)
            } else {
                useJUnitJupiter()
            }

            targets.all {
                testTask.configure {
                    testLogging {
                        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                        exceptionFormat = TestExceptionFormat.FULL

                        afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
                            if (desc.parent == null) {
                                val output = "${desc.displayName} >> Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} passed, ${result.failedTestCount} failed, ${result.skippedTestCount} skipped)"
                                val startItem = "|  "
                                val endItem = "  |"
                                val repeatLength = startItem.length + output.length + endItem.length
                                logger.lifecycle("-".repeat(repeatLength))
                                logger.lifecycle("$startItem$output$endItem")
                                logger.lifecycle("-".repeat(repeatLength))
                            }
                        }))
                    }

                    reports {
                        html.required = true
                        junitXml.required = true
                    }
                }
            }
        }

        val test by getting(JvmTestSuite::class)

        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
            }

            sources {
                java.setSrcDirs(setOf("src/integration-test/java"))
                kotlin.setSrcDirs(setOf("src/integration-test/kotlin"))
                resources.setSrcDirs(setOf("src/integration-test/resources"))
            }

            targets.all {
                testTask.configure {
                    shouldRunAfter(test)
                }
            }
        }

        tasks.named(JavaBasePlugin.CHECK_TASK_NAME).configure {
            dependsOn(integrationTest)
        }
    }
}
