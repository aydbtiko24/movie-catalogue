plugins {
    id(GradlePluginId.KOTLIN)
    id(GradlePluginId.KTLINT)
}

dependencies {
    implementation(Kotlin.stdlib)
    implementation(Coroutines.dependency)
    implementation(Android.pagingCommon)
}
