package com.betomorrow.gradle.extensions

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

internal fun parseXmlFile(xmlFile: File): Document {
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
    documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
    val xmlInput = InputSource(StringReader(xmlFile.readText()))

    return documentBuilder.parse(xmlInput)
}

internal fun Document.getElementsIteratorByXPath(path: String): Iterable<Node> {
    val xPathFactory = XPathFactory.newInstance()
    val xPath = xPathFactory.newXPath()

    val elements = xPath.evaluate(path, this, XPathConstants.NODESET) as NodeList

    return object : Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return iterator {
                for (i in 0 until elements.length) {
                    yield(elements.item(i))
                }
            }
        }
    }
}

internal fun Node.getAttribute(name: String): String {
    return this.attributes.getNamedItem(name).nodeValue
}
