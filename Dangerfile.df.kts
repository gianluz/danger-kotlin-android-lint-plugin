@file:DependsOn("./build/libs/danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT.jar")

import com.gianluz.dangerkotlin.androidlint.AndroidLint
import systems.danger.kotlin.*

val danger = Danger(args)

AndroidLint.report("./build/resources/test/lint-results.xml")