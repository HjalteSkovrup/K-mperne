plugins {
    kotlin("jvm") version "1.6.10"
}

group = "me.hjalt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

sourceSets.main {
    java.srcDirs( "src/main/kotlin")
}
