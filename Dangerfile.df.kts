@file:DependsOn("org.apache.commons:commons-text:1.6")
//@file:Repository(url="file:///build/libs")
//@file:DependsOn("/Users/gianlucazuddas/Repos/danger-kotlin-android-lint-plugin/build/libs/danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT.jar")
//@file:DependsOn("com/gianluz/danger-kotlin-android-lint-plugin/0.0.1-SNAPSHOT/danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT.jar")
//@file:DependsOn("com.gianluz:danger-kotlin-android-lint-plugin:0.0.1-SNAPSHOT")
//@file:DependsOn("danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT.jar")

//@file:DependsOn(value="./build/libs/danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT.jar")

import org.apache.commons.text.WordUtils
import org.jetbrains.kotlin.script.util.*
import systems.danger.kotlin.*

val danger = Danger(args)

warn("Created files ${danger.git.createdFiles.size}")
warn(WordUtils.capitalize("test"))

//AndroidLint.report("./build/resources/test/lint-results.xml")