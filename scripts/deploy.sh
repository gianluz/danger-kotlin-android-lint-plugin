#!/usr/bin/env bash
gradle wrapper
./gradlew signMavenPublication
ls build/libs
./gradlew publishMavenPublicationToSonatypeRepository