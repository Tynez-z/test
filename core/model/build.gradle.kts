plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    //for annotations @Stable, @Immutable
    implementation(libs.androidx.compose.runtime)
}