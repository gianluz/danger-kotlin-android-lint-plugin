//@file:DependsOn("./build/libs/danger-kotlin-android-lint-plugin-0.0.1-SNAPSHOT.jar")
//@file:Repository("https://repo.maven.apache.org")
@file:DependsOn("org.apache.commons:commons-text:1.6")
//@file:DependsOn("com.gianluz:danger-kotlin-android-lint-plugin:0.0.1-SNAPSHOT")

import org.apache.commons.lang3.StringUtils
import systems.danger.kotlin.*

//register plugin AndroidLint

val danger = Danger(args)

//AndroidLint.report("./build/resources/test/lint-results.xml")

StringUtils.capitalize("wee")