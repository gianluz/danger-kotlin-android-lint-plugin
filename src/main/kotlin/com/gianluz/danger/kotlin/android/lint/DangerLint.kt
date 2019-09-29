package com.gianluz.danger.kotlin.android.lint

import com.danger.dangerkotlin.warn
import com.gianluz.danger.kotlin.android.lint.clean.GetLintsUseCase
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.filefilter.WildcardFileFilter

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

    /**
     * @param lintFile the path to the lint results file
     * @return a com.gianluz.danger.kotlin.android.lint.domain.model.Issues object
     */
    fun getLints(lintFile: String) = GetLintsUseCase().execute(lintFile)

    fun report() {
        warn("Report")
    }
}
