[![Maven Central](https://img.shields.io/maven-central/v/com.gianluz/danger-kotlin-android-lint-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.gianluz%22%20AND%20a:%22danger-kotlin-android-lint-plugin%22)
[![Build Status](https://travis-ci.org/gianluz/danger-kotlin-android-lint-plugin.svg?branch=master)](https://travis-ci.org/gianluz/danger-kotlin-android-lint-plugin)
# danger-kotlin-android-lint-plugin

Show the Android lint errors on your PR with [Danger Kotlin]

##Setup

Install and run [Danger Kotlin] as normal and in your `Dangerfile.df.kts` add the following dependency:
```kotlin
@file:DependsOn("com.gianluz:danger-kotlin-android-lint-plugin:0.0.3")
```
Then register your plugin before the `danger` initialisation and use the plugin:
```kotlin
register plugin AndroidLint

val danger = Danger(args)

AndroidLint.report("/path/to/the/androidlint/result/file.xml")
```
You can report more than one lint file.

##Configuration

You can customise the aspect of your reports defining the configuration file `androidlint.dangerplugin.yml`
```yaml
logLevel: WARNING
format: "{severity}: {message}"
failIf:
  warnings: 3
  errors: 1
  fatals: 1
  total: 3
```

####Accepted values are:

| **SETTING**  | **DESCRIPTION**                                                                                                                                   |                **DEFAULT**                | **ACCEPTED VALUES**                                                                                                                |
|----------|-----------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------:|--------------------------------------------------------------------------------------------------------------------------------|
| `logLevel` | Report all the lints with `severity` >= `logLevel`                                                                                                | `WARNING`                               | `WARNING, ERROR, FATAL`                                                                                                          |
| `format`   | Define a custom message for your lints                                                                                                        | `"{severity}: {message}"`               | `{id}, {severity}, {message}, {category}`,<br>`{priority}, {summary}, {explanation}, {url},`<br>`{urls}, {errorLine1}, {errorLine2}` |
| `failIf`   | Fail your PR if the condition is satisfied:<br>for example in this case will fail if there are at least:<br>3 warnings or 1 error or 1 fatal. | `warnings: 3`<br>`errors: 1`<br>`fatals: 1` | `warnings: Int`<br>`errors: Int`<br>`fatals: Int`<br>`total: Int`                                                                      |


[Danger Kotlin]:https://github.com/danger/kotlin
