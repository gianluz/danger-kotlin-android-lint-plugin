package com.gianluz.dangerkotlin.androidlint

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream

object ConfigurationParser {

    fun parse(file: String): Configuration {
        val yaml = Yaml()
        val config: Map<String, Any> = yaml.load(File(file).inputStream())

        val logLevel = LogLevel.valueOf(config["logLevel"] as String)
        val format = config["format"] as String
        val failIf = config["failIf"] as Array<*>

        return Configuration(logLevel, format)
    }
}