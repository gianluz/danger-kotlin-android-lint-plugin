@file:DependsOn("./build/libs/danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT.jar")

import systems.danger.kotlin.*
import com.gianluz.dangerkotlin.androidlint.AndroidLint

register plugin AndroidLint

val danger = Danger(args)

AndroidLint.report("./build/resources/test/lint-results.xml")