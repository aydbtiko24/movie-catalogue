Movie Catalogue 🎞️
==================
[![build](https://github.com/aydbtiko24/movie-catalogue/actions/workflows/build.yml/badge.svg)](https://github.com/aydbtiko24/movie-catalogue/actions/workflows/build.yml)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![GitHub license](https://img.shields.io/github/license/aydbtiko24/movie-catalogue.svg?style=plastic)](https://github.com/aydbtiko24/movie-catalogue/blob/main/LICENSE)
<img align="right" src="https://github.com/aydbtiko24/movie-catalogue/blob/main/media/screen.gif" alt="screen" width="250" height="500" style="display: inline; float:right;"/>

Movie Catalogue is a project "base on" my last submission at [Dicoding Belajar Android Jetpack Pro](https://www.dicoding.com/academies/129) which currently has 3 main screens to display A list of Movies, Tv-Shows, and Favourite.

Uses [TMDB API](https://www.themoviedb.org/documentation/api) to load Movies And Tv Shows data, if you willing to clone this repo don't forget to 
[get the Api Key from TMDB](https://developers.themoviedb.org/3/getting-started/introduction), once you get the key copy the default API value from 
[api.properties](main/api.properties) replace the empty string with your API key and add into your locale.properties file.

## Architecture
<p align="left">
<img src="https://github.com/aydbtiko24/movie-catalogue/blob/main/media/clean.png" alt="clean" width="550" height="213" style="display: inline;"/>
</p>

* [Presentation](main/app): A layer that interacts with the UI, mainly Android Stuff like Activities, Fragments, ViewModel, etc. It would include both domain and data layers.

* [Domain](main/domain): Contains the business logic of the application. It is the individual and innermost module. It’s a complete kotlin module.

* [Data](main/data): It includes the domain layer. It would implement the interface exposed by domain layer and dispenses data to app.

To allow the business to adapt to changing technology and interfaces. While the internet might move from desktop to mobile, or from mobile to virtual assistant, the core business remains the same. I decided to rewrite Movie Catalogue with [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).


## Libraries Used 🛠
- [Jetpack libraries][1] - Encompasses a collection of Android libraries that incorporate best practices and provide backwards compatibility in Android apps.
- [Material Design Components][1.1] - Modular and customizable Material Design UI components for Android.
- [Room][1.2] - Abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
- [Paging][1.3] - The Paging Library makes it easier for you to load data gradually and gracefully within your app's RecyclerView.
- [Navigation][1.4] - framework for navigating between 'destinations' within an Android application that provides a consistent API whether destinations are implemented as Fragments, Activities, or other components.
- [Kotlin Coroutines][2] - For managing background threads with simplified code and reducing needs for callbacks.
- [LiveData][18] - Observable data holder class.
- [Koin][3] - A pragmatic and lightweight dependency injection framework for Kotlin developers.
- [Retrofit][4] - A type-safe HTTP client for Android and Java.
- [Moshi][5] - A modern JSON library for Kotlin and Java.
- [Coil][6] - An image loading library for Android backed by Kotlin Coroutines.
- [Kotlin DSL][15] - Gradle’s Kotlin DSL provides an alternative syntax to the traditional Groovy.
- [Secrets Gradle Plugin for Android][16] - A Gradle plugin for providing your secrets securely to your Android project.
- [Ktlint Gradle][17] - Provides a convenient wrapper plugin over the ktlint project.

## Test Libraries Used 🛠
- [Junit][7] - Simple framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks.
- [Espresso][8] - To write concise, beautiful, and reliable Android UI tests.
- [Truth][9] - Library for performing assertions in tests.
- [MockK][10] - Mocking library for Kotlin.
- [Robolectric][11] - Framework that brings fast and reliable unit tests to Android. Tests run inside the JVM on your workstation in seconds.
- [UI Automator][12] - UI testing framework suitable for cross-app functional UI testing across system and installed apps.
- [MockWebServer][13] - A scriptable web server for testing HTTP clients.
- [OkHttp Idling Resource][14] - An Espresso IdlingResource for OkHttp.

[1]: https://developer.android.com/jetpack/androidx/explorer
[1.1]: https://material.io/develop/android
[1.2]: https://developer.android.com/jetpack/androidx/releases/room
[1.3]: https://developer.android.com/jetpack/androidx/releases/paging
[1.4]: https://developer.android.com/jetpack/androidx/releases/navigation
[2]: https://kotlinlang.org/docs/reference/coroutines-overview.html
[3]: https://insert-koin.io/
[4]: https://square.github.io/retrofit/
[5]: https://github.com/square/moshi
[6]: https://coil-kt.github.io/coil/
[7]: https://junit.org/junit4/
[8]: https://developer.android.com/training/testing/espresso
[9]: https://truth.dev/
[10]: https://mockk.io/
[11]: http://robolectric.org/
[12]: https://developer.android.com/training/testing/ui-automator
[13]: https://github.com/square/okhttp/tree/master/mockwebserver
[14]: https://github.com/JakeWharton/okhttp-idling-resource
[15]: https://docs.gradle.org/current/userguide/kotlin_dsl.html
[16]: https://github.com/google/secrets-gradle-plugin
[17]: https://github.com/JLLeitschuh/ktlint-gradle
[18]: https://developer.android.com/topic/libraries/architecture/livedata

## Discuss 💬
Doubts or want to present your opinions, views? You're always welcome. You can [start discussions](https://github.com/aydbtiko24/movie-catalogue/discussions).

## Published App 🏪
Want to see or maybe try my Published App on Google Play? [Let's go! I'll take you there!](https://bit.ly/apps-highair)😀.


## License
```
   Copyright [2021] [aydbtiko24]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
