object Gradle {

    const val buildTools = "com.android.tools.build:gradle:4.2.1"
    const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:10.0.0"
}

object Kotlin {

    private const val version = "1.5.10"
    const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
}

object Android {

    // ktx
    const val coreKtx = "androidx.core:core-ktx:1.5.0"

    // fragment
    const val fragment = "androidx.fragment:fragment-ktx:1.3.4"

    // app compat
    const val appCompat = "androidx.appcompat:appcompat:1.3.0"

    // material design
    const val materialDesign = "com.google.android.material:material:1.3.0"

    // constraint layout
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"

    // recycler view
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0"

    // swipe refresh layout
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    // liveData
    private const val liveDataVersion = "2.3.1"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$liveDataVersion"
    const val liveDataCommon = "androidx.lifecycle:lifecycle-common-java8:$liveDataVersion"

    // room
    private const val roomVersion = "2.3.0"
    const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
    const val roomKtx = "androidx.room:room-ktx:$roomVersion"
    const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    const val roomTestDependency = "androidx.room:room-testing:$roomVersion"

    // Paging
    private const val pagingVersion = "3.0.0"
    const val paging = "androidx.paging:paging-runtime-ktx:$pagingVersion"
    const val pagingCommon = "androidx.paging:paging-common-ktx:$pagingVersion"

    // navigation
    private const val navigationVersion = "2.3.5"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:$navigationVersion"
    const val navigationArgsPlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
}

// Koin
object Koin {

    private const val version = "2.2.2"
    const val dependency = "io.insert-koin:koin-core:$version"
    const val testDependency = "io.insert-koin:koin-test:$version"
    const val android = "io.insert-koin:koin-android:$version"
    const val viewModel = "io.insert-koin:koin-androidx-viewmodel:$version"
    const val scope = "io.insert-koin:koin-androidx-scope:$version"
}

// coroutines
object Coroutines {

    private const val version = "1.5.0"
    const val dependency = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    const val testDependency = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
}

// retrofit
object Retrofit {

    private const val version = "2.9.0"
    const val dependency = "com.squareup.retrofit2:retrofit:$version"
    const val converter = "com.squareup.retrofit2:converter-moshi:$version"
}

// coil
object Coil {

    const val dependency = "io.coil-kt:coil:1.2.1"
}

// moshi
object Moshi {

    private const val version = "1.12.0"
    const val dependency = "com.squareup.moshi:moshi-kotlin:$version"
    const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
}

// logging
object Logging {

    const val okhttp = "com.squareup.okhttp3:logging-interceptor:4.9.1"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
}
