plugins {
    id(GradlePluginId.ANDROID_APPLICATION)
    id(GradlePluginId.KOTLIN_ANDROID)
    id(GradlePluginId.KOTLIN_PARCELIZE)
    id(GradlePluginId.KOTLIN_KAPT)
    id(GradlePluginId.NAVIGATION_SAFE_ARGS)
    id(GradlePluginId.KTLINT)
}

android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)
    buildToolsVersion(AndroidConfig.buildToolsVersion)

    defaultConfig {
        applicationId = "com.dicodingjetpackpro.moviecatalogue"
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures.dataBinding = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(Modules.domain))
    implementation(project(Modules.data))

    implementation(Kotlin.stdlib)
    implementation(Coroutines.dependency)

    implementation(Android.coreKtx)
    implementation(Android.fragment)
    implementation(Android.appCompat)
    implementation(Android.materialDesign)
    implementation(Android.constraintLayout)
    implementation(Android.recyclerView)
    implementation(Android.swipeRefreshLayout)
    implementation(Android.liveData)
    implementation(Android.liveDataCommon)
    implementation(Android.paging)
    implementation(Android.navigationFragment)
    implementation(Android.navigationUI)

    implementation(Koin.dependency)
    implementation(Koin.android)
    implementation(Koin.viewModel)
    implementation(Koin.scope)

    implementation(Coil.dependency)

    implementation(Moshi.dependency)
    kapt(Moshi.codegen)

    implementation(Retrofit.dependency)
    implementation(Retrofit.converter)

    implementation(Logging.okhttp)
    implementation(Logging.timber)
    /** TESTING DEPENDENCIES */
    testImplementation(project(Modules.sharedTest))
    testImplementation(TestDependencies.mockk)

    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.junitExt)
    androidTestImplementation(TestDependencies.junitExt)

    androidTestImplementation(TestDependencies.espressoCore)
    androidTestImplementation(TestDependencies.espressoContrib)
    androidTestImplementation(TestDependencies.espressoIntents)

    testImplementation(TestDependencies.androidxTestCore)
    testImplementation(TestDependencies.androidxTestRunner)
    testImplementation(TestDependencies.androidxTestRules)
    androidTestImplementation(TestDependencies.androidxTestRunner)
    androidTestImplementation(TestDependencies.androidxTestRules)
    androidTestUtil(TestDependencies.androidxOrchestrator)

    testImplementation(TestDependencies.archTesting)
    androidTestImplementation(TestDependencies.archTesting)

    testImplementation(TestDependencies.truth)

    testImplementation(Coroutines.testDependency)
    androidTestImplementation(Coroutines.testDependency) {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-debug")
    }

    androidTestImplementation(Koin.testDependency)

    implementation(TestDependencies.idlingResource)

    androidTestImplementation(Android.roomTestDependency)

    androidTestImplementation(TestDependencies.uiautomator)

    androidTestImplementation(TestDependencies.mockWebServer)

    androidTestImplementation(TestDependencies.okhttp3IdlingResource)
}
