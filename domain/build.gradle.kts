plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies{
    implementation(libs.kotlinx.coroutine)
    //paging3 - common(안드로이드 의존성 없음)
    implementation(libs.androidx.paging.common)
    implementation(libs.serialization.json)

}