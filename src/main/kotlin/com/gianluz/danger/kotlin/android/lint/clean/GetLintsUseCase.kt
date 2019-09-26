package com.gianluz.danger.kotlin.android.lint.clean

import com.gianluz.danger.kotlin.android.lint.domain.model.Issues
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class GetLintsUseCase {
    fun execute(filePath: String): Issues {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()

        val document = builder.parse(File(filePath))
        val rootElement = document.documentElement
        val issues = arrayListOf<Issues.Issue>()
        val lintVersion = rootElement.getAttribute("by")
        val elements = rootElement.getElementsByTagName("issue")
        for (x in 0 until elements.length) {
            with(elements.item(x) as Element) {
                if(this.nodeType == Node.ELEMENT_NODE) {
                    val issue = Issues.Issue(
                        id = getAttribute("id"),
                        severity = getAttribute("severity"),
                        message = getAttribute("message"),
                        category = getAttribute("category"),
                        priority = getAttribute("priority"),
                        summary = getAttribute("summary"),
                        explanation = getAttribute("explanation"),
                        url = getAttribute("url"),
                        urls = getAttribute("urls"),
                        errorLine1 = getAttribute("errorLine1"),
                        errorLine2 = getAttribute("errorLine2"),
                        location = getLocation()
                    )
                    issues.add(
                        issue
                    )
                }
            }
        }

        return Issues(
            issues,
            lintVersion
        )
    }

    //private fun Node.getAttribute(name: String) = attributes.getNamedItem(name).nodeValue

    private fun Element.getLocation(): Issues.Issue.Location {
        return (getElementsByTagName("location").item(0) as Element).run {
            Issues.Issue.Location(
                file = getAttribute("file"),
                line = getAttribute("line"),
                column = getAttribute("column")
            )
        }
    }
}