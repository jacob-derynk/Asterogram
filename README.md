# Asterogram ☄️

> Instagram but for asteroids 🪐🚀👾

This is a showcase native Android app written in Kotlin. It uses [Meteorite Landings API](https://dev.socrata.com/foundry/data.nasa.gov/y77d-th95) provided by NASA to show feed of all of the known meteorites landings. It is still in progress.

This app so far features:

- MVVM pattern and usage of Jetpack libraries with recommended patterns
- User onboarding with username set up
- Showing and paging a feed of asteroids in the interface similar to Instagram
- Bookmarking and saving favorite posts into local DB for offline usage
- "Random image generation" so that every asteroid is unique
- Basic error handling when fetching fails

Things to look forward to:

- Unit tests and UI tests
- Linking posts, comments and sharing
- Un-saving post in Profile
- Show asteroid in map as we have lat long so it's easy to do

It's just a smol showcase app, so be nice to it :)

## Starting things up 🚀

- **Android SDK** installed and defined as `sdk.dir` variable in `local.properties`.
- **appToken** variable inside your `local.properties`. You can obtain one by signing up on [data.nasa.gov](https://data.nasa.gov/) and then following a few steps described in [Generating App Tokens and API Keys](https://support.socrata.com/hc/en-us/articles/210138558-Generating-App-Tokens-and-API-Keys).

You can then build the project from the terminal using `./gradlew build` or assemble debug APK `./gradlew :app:assembleDebug`. Signing config is not configured yet, so no release APK here :)

## Tech used ⚙️

- Jetpack Compose for UI with Material 3
- Koin for DI
- Retrofit and okhttp3 for networking
- Moshi for JSON parsing
- Timber for better Log experience
- Coil for images
- Datastore for saving small values like username
- Room for database

## Bugs and improvements 🐛

See an issue? Open a new issue here on GitHub. Have something to add? Open PR and explain why and what you've done. If it's not possible, I probably forgot to allow PR's and issues, sry.