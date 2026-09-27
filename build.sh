# Gradle wrapper script for Jasi Modern
# This is a simplified version - in production, use official gradle wrapper

#!/bin/sh
# Jasi Modern Build Script

echo "================================"
echo "Jasi Modern - Build Script"
echo "================================"
echo ""

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo "Error: ANDROID_HOME not set"
    echo "Please set ANDROID_HOME to your Android SDK path"
    exit 1
fi

# Build the project
echo "Building Jasi Modern..."
./gradlew assembleRelease

# Check if build succeeded
if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful!"
    echo "APK location: app/build/outputs/apk/release/app-release.apk"
else
    echo ""
    echo "Build failed!"
    exit 1
fi