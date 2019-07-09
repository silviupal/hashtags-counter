plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

//apply plugin: "io.fabric"

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTools)
    defaultConfig {
        applicationId = ApplicationId.id
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Releases.versionCode
        versionName = Releases.versionName
        testInstrumentationRunner = TestLibraries.testInstrumentationRunner
    }
    signingConfigs {
        create("release") {
            storeFile = file("C:/android/Projects/hashtagscounter/keystore.jks")
            storePassword = "dancesport"
            keyAlias = "Hashtags Counter"
            keyPassword = "dancesport"
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = Debug.idSuffix
            versionNameSuffix = Debug.nameSuffix
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        create("beta") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility to Versions.compileOptions
        targetCompatibility to Versions.compileOptions
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libraries.kotlin)
    implementation(Libraries.material)
    implementation(AndroidxLibraries.constraintLayout)
    implementation(AndroidxLibraries.recyclerView)
    implementation(AndroidxLibraries.coreKtx)
    implementation(AndroidxLibraries.fragmentKtx)

    // Coroutines
    implementation(Libraries.coroutinesCore)
    implementation(Libraries.coroutinesAndroid)

    // UI Components 3rd
    implementation(Libraries.materialDrawer)
    implementation(Libraries.fastAdapter)

    // ViewModel
    implementation(Libraries.lifecycleExt)
    implementation(Libraries.lifecycleVM)

    // RxJava
    implementation(Libraries.rxJava)
    implementation(Libraries.rxAndroid)

    // Room
    implementation(Libraries.roomRuntime)
    kapt(Libraries.roomCompiler)

    // Dependency Injection
    implementation(Libraries.dagger)
    kapt(Libraries.daggerCompiler)

    // Sending events from a screen to another
    implementation(Libraries.eventBus)

    // For logs
    implementation(Libraries.timber)

    // EditText with drawable at the end
    implementation(Libraries.editDrawable)

    /* implementation("com.crashlytics.sdk.android:crashlytics:2.9.9@aar") {
     transitive = true;
     }*/

    testImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.runner)
    androidTestImplementation(TestLibraries.espressoCore)
}
