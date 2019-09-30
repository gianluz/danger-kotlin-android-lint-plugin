package com.gianluz.danger.kotlin.android.lint

import com.danger.dangerkotlin.FilePath
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

    /**
     * Write the default report provided by danger kotlin android lint plugin
     * @param lintFile the lint report file path
     */
    fun report(lintFile: String) {
        with(getLints(lintFile)) {
            var hasErrorsOrFatals = 0
            issues.forEach {
                if (it.severity == "Error" || it.severity == "Fatal") {
                    hasErrorsOrFatals++
                }
                warn("${it.severity}: ${it.message}",
                    it.location.file,
                    Integer.parseInt(it.location.line)
                )
            }
            if(hasErrorsOrFatals>0) {
                error("Danger Kotlin Lint Plugin finished with $hasErrorsOrFatals errors")
            }
        }
    }
}
