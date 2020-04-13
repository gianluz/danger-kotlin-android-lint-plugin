package com.gianluz.dangerkotlin.androidlint

import com.gianluz.dangerkotlin.androidlint.model.domain.Issues
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.filefilter.WildcardFileFilter
import systems.danger.kotlin.sdk.DangerPlugin

fun androidLint(block: AndroidLint.() -> Unit): Unit = AndroidLint.run(block)

object AndroidLint : DangerPlugin() {

    override val id: String
        get() = this.javaClass.name

    private val configuration by lazy {
        ConfigurationParser.parse("androidlint.dangerplugin.yml")
    }

    /**
     * @param projectDir the root project or module dir
     * @param wildCard the file name: eg "lint-results.xml"
     * @return a List of lint files path
     */
    fun find(projectDir: String, wildCard: String): List<String> {
        return FileUtils.listFiles(File(projectDir), WildcardFileFilter(wildCard), null)
            .map {
                it.absolutePath
            }
    }

    /**
     * @param projectDir the root project or module dir
     * @param wildCards the file names: eg "lint-results.xml", "lint-results-debug.xml", "lint-results-release.xml"
     * @return a List of lint files path
     */
    fun find(projectDir: String, vararg wildCards: String): List<String> {
        return wildCards.fold(mutableListOf()) { acc: MutableList<String>, wildCard: String ->
            acc.apply {
                addAll(find(projectDir, wildCard))
            }
        }.toList()
    }

    /**
     * @param lintFilePath the path to the lint results file
     * @return a List of issues
     */
    fun parse(lintFilePath: String): List<Issues.Issue> = LintParser.parse(lintFilePath).issues

    /**
     * @param lintFilePaths the paths to the lint results files
     * @return a List of issues
     */
    fun parseAll(vararg lintFilePaths: String): List<Issues.Issue> {
        return lintFilePaths.fold(mutableListOf()) { acc: MutableList<Issues.Issue>, lintFile ->
            acc.apply {
                addAll(parse(lintFile))
            }
        }.toList()
    }

    /**
     * @param lintFilePaths the paths to the lint results files
     * @return a List of distinct issues
     */
    fun parseAllDistinct(vararg lintFilePaths: String): List<Issues.Issue> {
        return parseAll(*lintFilePaths).distinct()
    }

    /**
     * Write the default report provided by danger-kotlin-android-lint-plugin for all lintFiles
     * @param lintFiles the lint report file path
     */
    fun report(vararg lintFiles: String) {
        report(parseAll(*lintFiles))
    }

    /**
     * Write the default report provided by danger-kotlin-android-lint-plugin for distinct issues in lintFiles
     * @param lintFiles the lint report file path
     */
    fun reportDistinct(vararg lintFiles: String) {
        report(parseAllDistinct(*lintFiles))
    }

    /**
     * Write the default report provided by danger-kotlin-android-lint-plugin for a List<Issue.Issues>
     * @param issues the list of issues
     */
    fun report(issues: List<Issues.Issue>) {
        issues.filter { issue ->
            issue.severity.asLogLevel().ordinal >= configuration.logLevel.ordinal
        }.forEach { issue ->
            val message = configuration.messageFormat.format(issue)
            context.warn(
                message,
                issue.location.file.replace(System.getProperty("user.dir"), ""),
                issue.location.line.asInt()
            )
        }

        configuration.failIf.forEach {
            when (it) {
                is FailIf.Warnings -> {
                    handleFailure(
                        it.min,
                        issues.filter { issue -> issue.severity.asLogLevel() == LogLevel.WARNING },
                        "warnings"
                    )
                }
                is FailIf.Errors -> {
                    handleFailure(
                        it.min,
                        issues.filter { issue -> issue.severity.asLogLevel() == LogLevel.ERROR },
                        "errors"
                    )
                }
                is FailIf.Fatals -> {
                    handleFailure(
                        it.min,
                        issues.filter { issue -> issue.severity.asLogLevel() == LogLevel.FATAL },
                        "fatals"
                    )
                }
                is FailIf.Total -> {
                    handleFailure(it.min, issues, "total lints")
                }
            }
        }
    }

    private fun handleFailure(min: Int, lints: List<Issues.Issue>, failureType: String) {
        if (lints.size >= min) {
            context.fail("danger-kotlin-android-lint-plugin failed with ${lints.size} $failureType")
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
