package com.gianluz.dangerkotlin.androidlint

import com.gianluz.dangerkotlin.androidlint.model.domain.Issues
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import systems.danger.kotlin.sdk.DangerContext

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AndroidLintTest {

    @RelaxedMockK
    lateinit var context: DangerContext

    @BeforeEach
    fun startUp() {
        clearAllMocks()
        AndroidLint.context = context
        mockkObject(LintParser, ConfigurationParser, AndroidLint)
        every { LintParser.parse(ANY_TEST_LINT_FILE) } returns ISSUES
    }

    @Test
    fun givenDefaultConfiguration_whenReport_thenFailsWithOneError() {
        givenDefaultConfiguration()

        AndroidLint.report(ANY_TEST_LINT_FILE)

        verify(ordering = Ordering.ORDERED) {
            context.warn("Error: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.fail("danger-kotlin-android-lint-plugin failed with 1 errors")
        }
    }

    @Test
    fun givenNeverFailsConfiguration_whenReport_thenNeverFails() {
        givenNeverFailsConfiguration()

        AndroidLint.report(ANY_TEST_LINT_FILE)

        verify(ordering = Ordering.ORDERED) {
            context.warn("Error: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.warn("Warning: message", "file", 1)
        }

        verify(exactly = 0) {
            context.fail(any())
        }
    }

    @Test
    fun givenTotalFailuresConfiguration_whenReport_thenFailsWithThreeTotals() {
        givenTotalFailuresConfiguration()

        AndroidLint.report(ANY_TEST_LINT_FILE)

        verify(ordering = Ordering.ORDERED) {
            context.warn("Error: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.fail("danger-kotlin-android-lint-plugin failed with 3 total lints")
        }
    }

    @Test
    fun givenTotalFailuresConfiguration_whenReportOnMultipleFiles_thenFailsWithThreeTotals() {
        givenTotalFailuresConfiguration()

        AndroidLint.report(ANY_TEST_LINT_FILE, ANY_TEST_LINT_FILE)

        verify(ordering = Ordering.ORDERED) {
            context.warn("Error: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.warn("Error: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.fail("danger-kotlin-android-lint-plugin failed with 6 total lints")
        }
    }

    @Test
    fun givenTotalFailuresConfigurationAndDuplicates_whenReportDistinct_thenFailsWithThreeTotals() {
        givenTotalFailuresConfiguration()

        AndroidLint.reportDistinct(ANY_TEST_LINT_FILE, ANY_TEST_LINT_FILE)

        verify(ordering = Ordering.ORDERED) {
            context.warn("Error: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.warn("Warning: message", "file", 1)
            context.fail("danger-kotlin-android-lint-plugin failed with 3 total lints")
        }
    }

    @Test
    fun givenLogLevelErrorConfiguration_whenReport_thenLogOnlyOneErrorAndFails() {
        givenLogLevelErrorConfiguration()

        AndroidLint.report(ANY_TEST_LINT_FILE)

        verify(ordering = Ordering.ORDERED) {
            context.warn("Error: message", "file", 1)
            context.fail("danger-kotlin-android-lint-plugin failed with 1 errors")
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    private fun givenDefaultConfiguration() {
        every {
            AndroidLint getProperty "configuration"
        } returns Configuration()
    }

    private fun givenNeverFailsConfiguration() {
        every {
            AndroidLint getProperty "configuration"
        } returns Configuration(failIf = arrayOf())
    }

    private fun givenTotalFailuresConfiguration() {
        every {
            AndroidLint getProperty "configuration"
        } returns Configuration(failIf = arrayOf(FailIf.Total(3)))
    }

    private fun givenLogLevelErrorConfiguration() {
        every {
            AndroidLint getProperty "configuration"
        } returns Configuration(LogLevel.ERROR)
    }

    private companion object {
        private const val ANY_TEST_LINT_FILE = "testlintfile.xml"
        private val ISSUES = Issues(
            arrayListOf(
                Issues.Issue(
                    "abc",
                    "Error",
                    "message",
                    "category",
                    "priority",
                    "summary",
                    "explanation",
                    "url",
                    "urls",
                    "errorLine1",
                    "errorLine2",
                    Issues.Issue.Location("file", "1", "1")
                ),
                Issues.Issue(
                    "abcd",
                    "Warning",
                    "message",
                    "category",
                    "priority",
                    "summary",
                    "explanation",
                    "url",
                    "urls",
                    "errorLine1",
                    "errorLine2",
                    Issues.Issue.Location("file", "1", "1")
                ),
                Issues.Issue(
                    "abcde",
                    "Warning",
                    "message",
                    "category",
                    "priority",
                    "summary",
                    "explanation",
                    "url",
                    "urls",
                    "errorLine1",
                    "errorLine2",
                    Issues.Issue.Location("file", "1", "1")
                )
            ),
            "version"
        )
    }
}