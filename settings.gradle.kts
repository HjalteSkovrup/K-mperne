rootProject.name = "kæmperne"
include("server")
include("client")
include("general")


pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}