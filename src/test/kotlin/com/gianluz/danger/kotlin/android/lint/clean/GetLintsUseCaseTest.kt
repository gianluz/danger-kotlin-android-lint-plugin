package com.gianluz.danger.kotlin.android.lint.clean

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File



class GetLintsUseCaseTest {

    private val subject = GetLintsUseCase()

    @Test
    fun execute() {
        val issues = subject.execute(getFilePathFromResources())
        assertEquals(issues.version, "lint 3.4.0")
        assertEquals(issues.issues.size, 3)
        with(issues.issues[0]) {
            assertEquals(id, "OldTargetApi")
            assertEquals(severity, "Warning")
            assertEquals(message.contains("Not targeting the latest version"), true)
            assertEquals(category, "Correctness")
            assertEquals(priority, "6")
            assertEquals(summary.contains("Target SDK"), true)
            assertEquals(explanation.contains("When your application runs on a version"), true)
        } // etc.. TODO: write all assertions
    }

    private fun getFilePathFromResources(): String {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource("lint-results.xml").file)
        val absolutePath = file.absolutePath

        println(absolutePath)
        return absolutePath
    }
}
