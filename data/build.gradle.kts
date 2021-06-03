plugins {
    id(GradlePluginId.ANDROID_LIBRARY)
    id(GradlePluginId.KOTLIN_ANDROID)
    id(GradlePluginId.KTLINT)
    id(GradlePluginId.KOTLIN_KAPT)
    id(GradlePluginId.GOOGLE_SECRETS_GRADLE) version "0.6"
}

android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)

    defaultConfig {
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.plusAssign(
                    hashMapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }

        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(Modules.domain))

    implementation(Kotlin.stdlib)

    implementation(Coroutines.dependency)

    implementation(Android.pagingCommon)

    implementation(Android.roomRuntime)
    implementation(Android.roomKtx)
    kapt(Android.roomCompiler)

    implementation(Koin.dependency)

    implementation(Moshi.dependency)
    kapt(Moshi.codegen)

    implementation(Retrofit.dependency)
    implementation(Retrofit.converter)

    implementation(Logging.okhttp)
    implementation(Logging.timber)
    /** TESTING DEPENDENCIES */
    testImplementation(project(Modules.sharedTest))

    testImplementation(TestDependencies.archTesting)

    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.junitExt)

    testImplementation(TestDependencies.truth)

    testImplementation(Coroutines.testDependency)

    testImplementation(TestDependencies.robolectric)

    testImplementation(TestDependencies.mockk)
}

secrets {
    // A properties file containing default API_KEY values.
    defaultPropertiesFileName = "api.properties"
}
