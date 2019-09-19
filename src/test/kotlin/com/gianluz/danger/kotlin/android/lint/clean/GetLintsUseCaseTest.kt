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
    }

    private fun getFilePathFromResources(): String {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource("lint-results.xml").file)
        val absolutePath = file.absolutePath

        println(absolutePath)
        return absolutePath
    }
}
