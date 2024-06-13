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
	
	//Ktor
	implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    testImplementation("io.ktor:ktor-server-test-host:2.3.5")
    implementation("io.ktor:ktor-client-core:2.x.x")
    implementation("io.ktor:ktor-client-cio:2.x.x")
    implementation("io.ktor:ktor-client-content-negotiation:2.x.x")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.x.x")
    testImplementation("io.ktor:ktor-server-tests:2.x.x")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}