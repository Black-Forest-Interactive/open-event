plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
//    id("org.jetbrains.kotlin.kapt") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.2.0"
    id("org.jetbrains.kotlin.plugin.jpa") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("org.sonarqube") version "6.2.0.5505"
    id("com.gradleup.shadow") version "8.3.8" apply false
    id("com.google.cloud.tools.jib") version "3.4.5" apply false
    id("io.micronaut.application") version "4.5.4" apply false
    id("io.micronaut.test-resources") version "4.5.4" apply false
    id("io.micronaut.aot") version "4.5.4" apply false
    id("net.researchgate.release") version "3.1.0"
    id("maven-publish")
    id("jacoco")
}

repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        mavenContent { snapshotsOnly() }
    }
    mavenCentral()
    maven("https://maven.tryformation.com/releases") {
        content {
            includeGroup("com.jillesvangurp")
        }
    }
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "jacoco")

    repositories {
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent { snapshotsOnly() }
        }
        mavenCentral()
        maven("https://maven.tryformation.com/releases") {
            content {
                includeGroup("com.jillesvangurp")
            }
        }
    }
}


sonar {
    properties {
        property("sonar.projectKey", "Black-Forrest-Development_open-event")
        property("sonar.organization", "black-forrest-development")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.core.codeCoveragePlugin", "jacoco")
        property("sonar.sources", "src/main")
    }
}

release {
    git {
        requireBranch.set("development")
    }
    pushReleaseVersionBranch.set("master")
}