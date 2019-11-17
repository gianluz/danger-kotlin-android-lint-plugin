package com.gianluz.dangerkotlin.androidlint.model.domain

data class Issues(
    val issues: List<Issue>,
    val version: String
) {
    data class Issue(
        val id: String,
        val severity: String,
        val message: String,
        val category: String,
        val priority: String,
        val summary: String,
        val explanation: String,
        val url: String,
        val urls: String,
        val errorLine1: String,
        val errorLine2: String,
        val location: Location

    ) {
        data class Location(
            val file: String,
            val line: String,
            val column: String
        )
    }
}