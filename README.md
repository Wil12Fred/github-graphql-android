# GitHub GraphQL user search (Android)

An Android app written as a take-home exercise for a job interview
(November 2019). It queries the **GitHub GraphQL API v4** with
[Apollo Android](https://github.com/apollographql/apollo-kotlin) (v1.2):

- the first screen searches GitHub users as you type and pages through the
  results 5 at a time (infinite scroll, cursor-based pagination);
- tapping a user opens a screen that lists repositories found with the search
  query `org:<login>` (name, owner, description).

![Screenshot](Screen%20Shot%202019-11-28%20at%206.41.44%20PM.png)

Stack: Kotlin, Apollo GraphQL codegen, RecyclerView, Glide, AndroidX.

## Requirements

- JDK 8 (the project uses Android Gradle Plugin 3.5.1 / Gradle 5.4.1, which do
  not run on modern JDKs)
- Android SDK with `platforms;android-28` (`ANDROID_HOME` pointing at it)
- a GitHub personal access token — the GraphQL API rejects anonymous requests

## Build

```sh
export JAVA_HOME=/path/to/jdk8
export ANDROID_HOME=$HOME/Android/Sdk
export GITHUB_TOKEN=<your token>     # see .env.example
./gradlew assembleDebug testDebugUnitTest
# -> app/build/outputs/apk/debug/app-debug.apk
```

The token is compiled into `BuildConfig.GITHUB_TOKEN`. It can also be given as
the Gradle property `githubToken` (e.g. in `~/.gradle/gradle.properties`).
Without it the app builds, but every query fails with HTTP 401.

Install on a device with `./gradlew installDebug` (minSdk 26, Android 8.0+).

## Layout

```
app/src/main/graphql/          GraphQL queries + GitHub schema.json (Apollo codegen input)
app/src/main/java/com/example/githubsearch/
  view/MainActivity.kt         user search with pagination
  view/RepositoriesActivity.kt repositories of the selected user
  adapters/                    RecyclerView adapters
  models/                      GithubUser, GithubRepo
  utilities/ApolloUtils.kt     Apollo client with the Authorization header
  utilities/Constants.kt       API URL + token from BuildConfig
```

## Author

Wilber Cutire – [Wil12Fred](https://github.com/wil12fred)
