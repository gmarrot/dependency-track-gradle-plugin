package com.betomorrow.gradle.jacoco

import com.betomorrow.gradle.extensions.getAttribute
import com.betomorrow.gradle.extensions.getElementsIteratorByXPath
import com.betomorrow.gradle.extensions.parseXmlFile
import java.io.File

class CoverageReport(xmlFile: File) {
    val counters: List<CoverageCounter>

    init {
        val report = parseXmlFile(xmlFile)
        val counterIterator = report.getElementsIteratorByXPath("/report/counter")
        counters = counterIterator.map {
            CoverageCounter(
                type = it.getAttribute("type"),
                covered = it.getAttribute("covered").toDouble(),
                missed = it.getAttribute("missed").toDouble(),
            )
        }
    }
}
