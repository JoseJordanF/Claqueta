plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.4.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    //Configuration
    implementation("com.typesafe:config:1.4.3")
    implementation("io.github.cdimascio:dotenv-java:2.2.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}