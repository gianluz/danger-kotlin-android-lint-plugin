package com.gianluz.danger.kotlin.android.lint

import com.gianluz.danger.kotlin.android.lint.clean.GetLintsUseCase
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.filefilter.WildcardFileFilter

val dangerLint = DangerLint()

class DangerLint {

    /**
     * @param projectDir the root project or module dir
     * @return a list of lint files path
     */
    fun scan(projectDir: String): List<String> {
        return FileUtils.listFiles(File(projectDir), WildcardFileFilter("lint-results.xml"), null)
            .toMutableList().map {
                it.absolutePath
            }
    }

    fun getLints(lintFile: String) = GetLintsUseCase().execute(lintFile)
}
