plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.4.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    testImplementation("io.mockk:mockk:1.12.0")

    //Configuration
    implementation("com.github.JoseJordanF:LibraryConfigProject:1.3.2")

    //Kotlin Logging
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.3.12")
    implementation("ch.qos.logback:logback-core:1.3.12")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}