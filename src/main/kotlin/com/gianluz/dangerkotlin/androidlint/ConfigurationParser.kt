package com.gianluz.dangerkotlin.androidlint

import org.yaml.snakeyaml.Yaml
import java.io.File

internal object ConfigurationParser {

    fun parse(file: String): Configuration {
        val yaml = Yaml()
        val config: Map<String, Any> = yaml.load(File(file).inputStream())

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