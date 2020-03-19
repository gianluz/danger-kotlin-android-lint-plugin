#!/usr/bin/env bash
openssl aes-256-cbc -K $encrypted_d8011ea95b1b_key -iv $encrypted_d8011ea95b1b_iv -in secret.asc.enc -out secret.asc -d
gradle wrapper
./gradlew publish