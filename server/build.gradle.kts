import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "me.hjalt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets.main {
    java.srcDirs( "src/main/kotlin")
}

application {

    mainClass.set( "ServerKt" )
}

dependencies {
    implementation(project(":general"))
}