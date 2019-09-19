package com.gianluz.danger.kotlin.android.lint.clean

import com.gianluz.danger.kotlin.android.lint.domain.model.Issues
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
        val childNodes = rootElement.childNodes
        for (x in 0..childNodes.length) {
            with(childNodes.item(x)) {
                issues.add(
                    Issues.Issue(
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
                )
            }
        }

        return Issues(
            issues,
            lintVersion
        )
    }

    private fun Node.getAttribute(name: String) = attributes.getNamedItem(name).nodeValue

    private fun Node.getLocation(): Issues.Issue.Location {
        return firstChild.attributes.run {
            Issues.Issue.Location(
                file = getAttribute("file"),
                line = getAttribute("line"),
                column = getAttribute("column")
            )
        }
    }
}