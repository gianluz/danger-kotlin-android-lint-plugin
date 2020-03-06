//@file:CompilerOptions("-jvm-target", "1.8")
//Uses a fatjar in local
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("com.gianluz:danger-kotlin-android-lint-plugin:0.0.1-SNAPSHOT")

import com.gianluz.dangerkotlin.androidlint.AndroidLint
import systems.danger.kotlin.*

register plugin AndroidLint

val danger = Danger(args)

AndroidLint.report("./build/resources/test/lint-results.xml")