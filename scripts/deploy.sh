#!/usr/bin/env bash
FILE_ENCRYPTED=secret.asc.enc
FILE_DECRYPTED=secret.asc
gpg --version
gpg2 --version
echo "Decrypting gpg key..."
openssl aes-256-cbc -K $encrypted_d8011ea95b1b_key -iv $encrypted_d8011ea95b1b_iv -in $FILE_ENCRYPTED -out $FILE_DECRYPTED -d > /dev/null
if test -f "$FILE_DECRYPTED"; then
    echo "$FILE_ENCRYPTED was decrypted"
    echo "Publishing artifacts..."
    ./gradlew publish
fi