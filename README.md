# BITask

## Running the app

### With Android Studio
#### Prerequisites
- Install Android Studio (Version used for the development = Android Studio Flamingo | 2022.2.1 Patch 2)
- Ensure `JDK17` is selected under Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle (You can use the bundled JBR distribution)
- An emulator or device with `SDK >= 24`, available in Device Manager
#### Steps
- Open the repository's root directory in Android Studio
- Select `app` module in run configuration
- Run the app

### With Gradle and adb
#### Prerequisites
- Install Gradle and Android Debug Bridge
- Gradle wrapper being aware of `JDK17` (set `JAVA_HOME` or `org.gradle.java.home` accordingly)
#### Steps
- Navigate to this repository's root directory
- Run `./gradlew :app:assembleDebug`
- Once the build finishes
- Run `adb install app/build/outputs/apk/debug/app-debug.apk`

### Manually
- Navigate to [v0.1.0](https://github.com/sp0rk/BITask-public/releases/tag/v0.1.0) on an Android device
- Download `app-debug.apk`
- Open it on your device to install it manually

## Design decisions and rationale
- The app was designed with offline-first approach, treating local database as the single source of truth
- Data sources were built in a separate module
- XML-based layouts and View hierarchy were used over Compose due to maturity difference between paging-compose (beta) and paging (stable)
- The current UI is based on the defaults available in the dependencies, purely for pace-reasons
- It was assumed that API authentication is out of scope as Github API provides free request quota

## Work left to be done, assuming no time constraints
- Add CI/CD with static code analysis, coverage reporting, smoke-tests, and deployments
- Extract some config values, such as pageSize into `buildConfigFields`
- Extract feature modules
- Add more unit tests, especially in `CommitRemoteMediator` usage in `CommitListViewModel`
- Extract repositories between useCases and data layer
- Cleanup dependency definitions and build scripts
- Extract proper design system and styling
- Write proper `androidx.recyclerview.selection` -> `paging` bridge to implement the commit selection properly
- Implement robust app-wide error handling and loading state solutions
