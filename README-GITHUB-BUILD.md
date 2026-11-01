# tvOS Glass Android TV launcher project

This archive contains the Android project generated from the source script supplied in the chat, plus a GitHub Actions workflow.

## Build the APK on GitHub (no Android Studio required)

1. Extract this ZIP.
2. Open your GitHub repository `tvos-glass`.
3. Upload the **contents inside the `tvOS-Glass` folder** to the repository root (not the outer folder itself).
4. Make sure the repository's default branch is `main`.
5. Open the **Actions** tab. The workflow named **Build tvOS Glass APK** should run after the files are pushed. You can also select it and click **Run workflow**.
6. When the run finishes successfully, open that workflow run and download the artifact named `tvos-glass-debug-apk`.

## Important notes

- The supplied generator included `gradle-wrapper.properties`, but did not include `gradlew`, `gradlew.bat`, or `gradle-wrapper.jar`. The included workflow therefore installs Gradle 8.5 directly and runs `gradle assembleDebug`.
- The project has not been compiled or tested in this environment. If GitHub Actions reports a build error, share the first red error section or the build log so it can be fixed.
- The project declares Android 9 (API 28) as its minimum Android version in its Gradle configuration. Actual Android TV compatibility still needs testing on the target TV.
