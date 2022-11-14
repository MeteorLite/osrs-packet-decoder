import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.24")

    implementation("org.ow2.asm:asm:9.3")
    implementation(group = "org.jetbrains", name = "annotations", version = "20.1.0")
    implementation(group = "org.ow2.asm", name = "asm", version = "9.3")
    implementation(group = "org.ow2.asm", name = "asm-util", version = "9.0")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.6")
    implementation(group = "com.google.guava", name = "guava", version = "23.2-jre")
    implementation(group = "meteor", name = "logger", version = "1.7.0")
    compileOnly("org.projectlombok:lombok:1.18.24")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}