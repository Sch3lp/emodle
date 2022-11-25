
plugins {
    kotlin("multiplatform") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    application
}

group = "be.swsb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

val ktorVersion = "2.1.0"
val kotlinCssVersion: String = "1.0.0-pre.356"
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder-jvm:$ktorVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css-jvm:$kotlinCssVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
    }
}

application {
    mainClass.set("be.swsb.application.ServerKt")
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}