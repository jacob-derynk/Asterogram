# Asterogram ☄️

> Instagram but for asteroids 🪐🚀👾

This is a showcase native Android app written in Kotlin. Come back later for some intergalactic adventures. 

## Starting things up 🚀

- **Android SDK** installed and defined as `sdk.dir` variable in `local.properties`.
- **appToken** variable inside your `local.properties`. You can obtain one by signing up on [data.nasa.gov](https://data.nasa.gov/) and then following a few steps described in [Generating App Tokens and API Keys](https://support.socrata.com/hc/en-us/articles/210138558-Generating-App-Tokens-and-API-Keys).

You can then build the project from the terminal using `./gradlew build`.

## Tech used ⚙️

- Jetpack Compose for UI 
- Koin for DI
- Retrofit and okhttp3 for networking
- Moshi for JSON parsing
- Timber for better Log experience
- Coil for images