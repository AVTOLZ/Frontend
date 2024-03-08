plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.23"
}

val os = org.gradle.internal.os.OperatingSystem.current()

val platform = when {
    os.isWindows -> "win"
    os.isMacOsX -> "mac"
    else -> "linux"
}

val jdkVersion = "17"

kotlin {
    androidTarget()

    jvm("desktop")

    js(IR) {
        browser()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.json)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.multiplatform.settings)
                implementation(libs.decompose.core)
                implementation(libs.decompose.compose)

                implementation(libs.magister.api)

                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.datetime)

            }
        }
        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)

                implementation(libs.ktor.client.logging.jvm)
                implementation(libs.ktor.client.json.jvm)
                implementation(libs.ktor.client.android)

                api("androidx.activity:activity-compose:1.8.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.12.0")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
        val desktopMain by getting {
            dependencies {
                dependsOn(commonMain)

                implementation(compose.desktop.common)

                implementation(libs.ktor.client.jvm)

                implementation("org.openjfx:javafx-base:$jdkVersion:${platform}")
                implementation("org.openjfx:javafx-graphics:$jdkVersion:${platform}")
                implementation("org.openjfx:javafx-controls:$jdkVersion:${platform}")
                implementation("org.openjfx:javafx-media:$jdkVersion:${platform}")
                implementation("org.openjfx:javafx-web:$jdkVersion:${platform}")
                implementation("org.openjfx:javafx-swing:$jdkVersion:${platform}")
            }
        }
        val jsMain by getting {
            dependencies {
                api(compose.html.core)

                implementation(libs.ktor.client.js)
            }
        }

    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}