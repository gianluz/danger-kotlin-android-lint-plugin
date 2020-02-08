package com.gianluz.dangerkotlin.androidlint

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class ConfigurationParserTest {

    @Test
    fun testYaml() {
        ConfigurationParser.parse(getFilePathFromResources())
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getFilePathFromResources(): String {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource("test.yaml").file)
        return file.absolutePath
    }

}