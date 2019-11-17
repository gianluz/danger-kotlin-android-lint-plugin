package com.gianluz.dangerkotlin.androidlint

import com.gianluz.dangerkotlin.androidlint.MessageFormat.Companion.DEFAULT_FORMAT
import com.gianluz.dangerkotlin.androidlint.model.domain.Issues
import java.io.File

data class Configuration(
    val logLevel: LogLevel = LogLevel.WARNING,
    val format: String = DEFAULT_FORMAT,
    val failIf: Array<FailIf> = arrayOf(FailIf.Errors(1), FailIf.Fatals(1))
) {
    companion object {
        private const val DEFAULT_CONFIG = "androidlint.dangerplugin.yml"

        @JvmStatic
        fun fromFile(file: String = DEFAULT_CONFIG): Configuration {
            return if (File(file).exists()) {
                Configuration()
            } else Configuration()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Configuration

        if (logLevel != other.logLevel) return false
        if (format != other.format) return false
        if (!failIf.contentEquals(other.failIf)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = logLevel.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + failIf.contentHashCode()
        return result
    }

    val messageFormat by lazy {
        MessageFormat(format)
    }
}

enum class LogLevel {
    WARNING,
    ERROR,
    FATAL
}

sealed class FailIf {
    data class Warnings(val min: Int) : FailIf()
    data class Errors(val min: Int) : FailIf()
    data class Fatals(val min: Int) : FailIf()
    data class Total(val min: Int) : FailIf()
}

class MessageFormat(private val format: String = DEFAULT_FORMAT) {
    companion object {
        const val ID = "{id}"
        const val SEVERITY = "{severity}"
        const val MESSAGE = "{message}"
        const val CATEGORY = "{category}"
        const val PRIORITY = "{priority}"
        const val SUMMARY = "{summary}"
        const val EXPLANATION = "{explanation}"
        const val URL = "{url}"
        const val URLS = "{urls}"
        const val ERROR_LINE_1 = "{errorLine1}"
        const val ERROR_LINE_2 = "{errorLine2}"
        const val DEFAULT_FORMAT = "$SEVERITY: $MESSAGE"
    }

    fun format(issue: Issues.Issue): String =
        format
            .replace(ID, issue.id)
            .replace(SEVERITY, issue.severity)
            .replace(MESSAGE, issue.message)
            .replace(CATEGORY, issue.category)
            .replace(PRIORITY, issue.priority)
            .replace(SUMMARY, issue.summary)
            .replace(EXPLANATION, issue.explanation)
            .replace(URL, issue.url)
            .replace(URLS, issue.urls)
            .replace(ERROR_LINE_1, issue.errorLine1)
            .replace(ERROR_LINE_2, issue.errorLine2)
}
