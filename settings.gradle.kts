pluginManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net") { name = "Fabric" }
        gradlePluginPortal()
    }

    plugins {
        val loomVersion: String by settings
        val kotlinVersion: String by System.getProperties()

        id("fabric-loom").version(loomVersion)
        kotlin("jvm").version(kotlinVersion)
    }
}
