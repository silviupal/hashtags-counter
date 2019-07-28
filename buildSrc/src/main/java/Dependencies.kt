object ApplicationId {
    val id = "silviupal.hashtagscounter"
}

object Releases {
    val versionCode = 5
    val versionName = "2.0"
}

object Modules {
    val app = ":app"
}

object Versions {
    val gradle = "3.4.1"
    val kotlin = "1.3.41"
    val compileSdk = 28
    val minSdk = 21
    val targetSdk = 28
    val buildTools = "29.0.0"
    val compileOptions = 1.8
    val material = "1.0.0"
    val constraintLayout = "1.1.3"
    val recyclerView = "1.0.0"
    val coreKtx = "1.0.2"
    val fragmentKtx = "1.0.0"
    val coroutinesCore = "1.1.1"
    val coroutinesAndroid = "1.1.1"
    val materialDrawer = "6.1.2"
    val fastAdapter = "3.3.1"
    val lifecycleExt = "2.0.0"
    val lifecycleVM = "2.0.0"
    val rxJava = "2.1.0"
    val rxAndroid = "2.0.1"
    val roomRuntime = "1.1.1"
    val roomCompiler = "1.1.1"
    val dagger = "2.13"
    val daggerCompiler = "2.13"
    val eventBus = "3.1.1"
    val timber = "4.7.1"
    val editDrawable = "1.1.0"
    val junit = "4.12"
    val runner = "1.2.0"
    val espressoCore = "3.2.0"
}

object Libraries {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val material = "com.google.android.material:material:${Versions.material}"
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
    val materialDrawer = "com.mikepenz:materialdrawer:${Versions.materialDrawer}"
    val fastAdapter = "com.mikepenz:fastadapter:${Versions.fastAdapter}"
    val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExt}"
    val lifecycleVM = "androidx.lifecycle:lifecycle-viewmodel:${Versions.lifecycleVM}"
    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    val roomRuntime = "android.arch.persistence.room:runtime:${Versions.roomRuntime}"
    val roomCompiler = "android.arch.persistence.room:compiler:${Versions.roomCompiler}"
    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerCompiler}"
    val eventBus = "org.greenrobot:eventbus:${Versions.eventBus}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val editDrawable = "com.github.MindorksOpenSource:EditDrawableText:${Versions.editDrawable}"
}

object Debug {
    val idSuffix = ".debug"
    val nameSuffix = "-debug"
}

object AndroidxLibraries {
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
}

object TestLibraries {
    val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val junit = "junit:junit:${Versions.junit}"
    val runner = "androidx.test:runner:${Versions.runner}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
}

object MainGradle {
    val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}
