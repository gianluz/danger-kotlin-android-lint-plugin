package com.gianluz.dangerkotlin.androidlint

import com.gianluz.dangerkotlin.androidlint.model.domain.Issues
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.filefilter.WildcardFileFilter
import systems.danger.kotlin.sdk.DangerPlugin

object AndroidLint : DangerPlugin() {

    override val id: String
        get() = this.javaClass.name

    private val configuration by lazy {
        ConfigurationParser.parse("androidlint.dangerplugin.yml")
    }

    /**
     * @param projectDir the root project or module dir
     * @return a list of lint files path
     */
    fun scan(projectDir: String): List<String> {
        return FileUtils.listFiles(File(projectDir), WildcardFileFilter("lint-results.xml"), null)
            .toMutableList().map {
                it.absolutePath
            }
    }

    /**
     * @param lintFile the path to the lint results file
     * @return a com.gianluz.danger.kotlin.android.lint.domain.model.Issues object
     */
    fun getLints(lintFile: String) = LintParser.parse(lintFile)

    /**
     * Write the default report provided by danger kotlin android lint plugin
     * @param lintFile the lint report file path
     */
    fun report(lintFile: String) {
        with(getLints(lintFile)) {

            issues.filter { issue ->
                issue.severity.asLogLevel().ordinal >= configuration.logLevel.ordinal
            }.forEach { issue ->
                val message = configuration.messageFormat.format(issue)
                context.warn(message, issue.location.file.replace(System.getProperty("user.dir"), ""), issue.location.line.asInt())
            }

            configuration.failIf.forEach {
                when (it) {
                    is FailIf.Warnings -> {
                        handleFailure(it.min, issues.filter { issue -> issue.severity.asLogLevel()==LogLevel.WARNING }, "warnings")
                    }
                    is FailIf.Errors -> {
                        handleFailure(it.min, issues.filter { issue -> issue.severity.asLogLevel()==LogLevel.ERROR }, "errors")
                    }
                    is FailIf.Fatals -> {
                        handleFailure(it.min, issues.filter { issue -> issue.severity.asLogLevel()==LogLevel.FATAL }, "fatals")
                    }
                    is FailIf.Total -> {
                        handleFailure(it.min, issues, "total lints")
                    }
                }
            }

        }
    }

    private fun handleFailure(min: Int, lints: List<Issues.Issue>, failureType: String) {
        if (lints.size >= min) {
            context.fail("Danger lint plugin failed with ${lints.size} $failureType")
        }
    }

    private fun String.asLogLevel(): LogLevel {
        return when (this) {
            "Error" -> LogLevel.ERROR
            "Fatal" -> LogLevel.FATAL
            else -> LogLevel.WARNING
        }
    }

    private fun String.asInt(): Int {
        return Integer.parseInt(this)
    }
}
