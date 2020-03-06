//@file:CompilerOptions("-jvm-target", "1.8")
//Uses a fatjar in local
@file:DependsOn("./build/libs/danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT-all.jar")

import com.gianluz.dangerkotlin.androidlint.AndroidLint
import systems.danger.kotlin.*

register plugin AndroidLint

val danger = Danger(args)

AndroidLint.report("./build/resources/test/lint-results.xml")