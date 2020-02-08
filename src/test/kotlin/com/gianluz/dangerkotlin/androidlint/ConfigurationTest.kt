package com.gianluz.dangerkotlin.androidlint

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.lang.IllegalArgumentException

class ConfigurationParserTest {

    @Test
    fun testYaml_success() {
        val config = ConfigurationParser.parse(getFilePathFromResources(SUCCESS))
        assertEquals(4, config.failIf.size)
        assertEquals("{severity}: {message}", config.format)
        assertEquals(LogLevel.WARNING, config.logLevel)
    }

    @Test
    fun testYaml_fail() {
        assertThrows<IllegalArgumentException> {
            ConfigurationParser.parse(getFilePathFromResources(FAIL))
        }
    }

    @Test
    fun testYaml_testNonNulls() {
        val config = ConfigurationParser.parse(getFilePathFromResources(NON_NULLS))
        assertEquals(1, config.failIf.size)
        assertEquals("{severity}: {message}", config.format)
        assertEquals(LogLevel.WARNING, config.logLevel)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getFilePathFromResources(testConf: String): String {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(testConf).file)
        return file.absolutePath
    }

    private companion object {
        private const val SUCCESS = "test.yaml"
        private const val FAIL = "testfail.yaml"
        private const val NON_NULLS = "testnonnulls.yaml"
    }

}