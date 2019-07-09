// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        //maven { url "https://maven.fabric.io/public" }
    }
    dependencies {
        classpath(MainGradle.gradle)
        classpath(MainGradle.kotlin)
        // classpath "io.fabric.tools:gradle:1.+"
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        // maven { url "https://maven.fabric.io/public" }
    }
}

tasks {
    val cleanTheProjectNow by registering(Delete::class) {
        delete(buildDir)
        println("Finished")
    }
}