import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)

    base

}




android {
    compileSdk = 35

    defaultConfig {
        applicationId = libs.versions.app.version.appId.get()
        minSdk = project.libs.versions.app.build.minimumSDK.get().toInt()
        targetSdk = project.libs.versions.app.build.targetSDK.get().toInt()
        versionName = project.libs.versions.app.version.versionName.get()
        versionCode = project.libs.versions.app.version.versionCode.get().toInt()
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }



    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = project.libs.versions.app.build.kotlinJVMTarget.get()
    }

    namespace = libs.versions.app.version.appId.get()

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation(libs.simple.mobile.tools.commons)
    implementation(libs.indicator.fast.scroll)
    implementation(libs.android.smsmms)
    implementation(libs.shortcut.badger)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.ez.vcard)
    implementation(libs.eventbus)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.room)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui.viewbinding)
    ksp(libs.androidx.room.compiler)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")

    implementation(platform("androidx.compose:compose-bom:2023.06.01"))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.runtime)

    //ssp
    implementation ("com.intuit.ssp:ssp-android:1.1.1")
    //sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.1")
}
