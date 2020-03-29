package com.gianluz.dangerkotlin.androidlint

import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import java.io.File

internal object ConfigurationParser {

    @Suppress("UNCHECKED_CAST")
    fun parse(file: String): Configuration {
        val loadSettings = LoadSettings.builder().build()
        val load = Load(loadSettings)
        val config: Map<String, Any> = load.loadFromInputStream(File(file).inputStream()) as Map<String, Any>

        val logLevel = LogLevel.valueOf(config["logLevel"] as String)
        val format = config["format"] as String
        val failIf = config["failIf"] as Map<*, *>

        val failIfList = failIf.map { entry ->
            val value = entry.value as Int
            when (entry.key as String) {
                "warnings" -> FailIf.Warnings(value)
                "errors" -> FailIf.Errors(value)
                "fatals" -> FailIf.Fatals(value)
                "total" -> FailIf.Total(value)
                else -> null
            }
        }

        return Configuration(logLevel, format, failIfList.filterNotNull().toTypedArray())
    }
}