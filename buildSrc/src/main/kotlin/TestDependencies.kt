object TestDependencies {
    // junit
    private const val junitVersion = "4.13.2"
    const val junit = "junit:junit:$junitVersion"
    private const val junitExtVersion = "1.1.2" // junit ext
    const val junitExt = "androidx.test.ext:junit:$junitExtVersion"

    // espresso
    private const val espressoVersion = "3.3.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:$espressoVersion"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:$espressoVersion"
    const val idlingResource = "androidx.test.espresso:espresso-idling-resource:$espressoVersion"

    // androidx test support
    private const val androidxTestVersion = "1.3.0"
    const val androidxTestCore = "androidx.test:core:$androidxTestVersion"
    const val androidxTestRunner = "androidx.test:runner:$androidxTestVersion"
    const val androidxTestRules = "androidx.test:rules:$androidxTestVersion"
    const val androidxOrchestrator = "androidx.test:orchestrator:$androidxTestVersion"

    // android arch core
    private const val archTestingVersion = "2.1.0"
    const val archTesting = "androidx.arch.core:core-testing:$archTestingVersion"

    // truth
    const val truth = "com.google.truth:truth:1.1"

    // mockk
    const val mockk = "io.mockk:mockk:1.11.0"

    // robolectric
    const val robolectric = "org.robolectric:robolectric:4.5.1"

    // uiautomator
    const val uiautomator = "androidx.test.uiautomator:uiautomator:2.2.0"

    // mock web server
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:4.9.1"

    // okhttp3-idling-resource
    const val okhttp3IdlingResource = "com.jakewharton.espresso:okhttp3-idling-resource:1.0.0"
}
