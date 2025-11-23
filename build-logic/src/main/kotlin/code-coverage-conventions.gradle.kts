import com.betomorrow.gradle.jacoco.CoverageReport

plugins {
    // Core
    jacoco
}

tasks.withType<JacocoCoverageVerification>().configureEach {
    // executionData(tasks.withType(Test::class.java)) should work, but it doesn't (./gradlew tasks fails)
    val testTasks = tasks.withType<Test>()
    executionData.builtBy(testTasks)
    executionData.from(files(testTasks).filter { it.name.endsWith(".exec") && it.exists() })
}

tasks.withType<JacocoReport>().configureEach {
    // executionData(tasks.withType(Test::class.java)) should work, but it doesn't (./gradlew tasks fails)
    val testTasks = tasks.withType<Test>()
    executionData.builtBy(testTasks)
    executionData.from(files(testTasks).filter { it.name.endsWith(".exec") && it.exists() })

    reports {
        html.required = true
        xml.required = true
    }

    doLast {
        val report = CoverageReport(reports.xml.outputLocation.asFile.get())

        val counter = report.counters.firstOrNull { it.type == "BRANCH" }
        if (counter != null) {
            val total = counter.missed + counter.covered
            val percentage = counter.covered / total * 100

            logger.lifecycle(
                "Coverage is ${"%.0f".format(percentage)}% " +
                    "(${"%.0f".format(counter.covered)} of ${"%.0f".format(total)} branches)"
            )
        }
    }
}
