plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.kotlin.kapt") version "1.7.20"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.7.20"
    id("org.sonarqube") version "3.5.0.2730"
    id("com.google.cloud.tools.jib") version "3.3.1" apply (false)
    id("io.micronaut.application") version "3.6.3" apply (false)
    jacoco
}

repositories {
    mavenCentral()
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "io.micronaut.application")
    apply(plugin = "org.gradle.jacoco")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")
        implementation("ch.qos.logback:logback-classic:1.4.4")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
        testImplementation("io.mockk:mockk:1.13.2")

        // https
        implementation("io.micronaut:micronaut-jackson-databind")
        implementation("io.micronaut:micronaut-http-client")

        // validation
        kapt("io.micronaut:micronaut-http-validation")
        implementation("io.micronaut:micronaut-validation")

        // openapi
        kapt("io.micronaut.openapi:micronaut-openapi")
        implementation("io.swagger.core.v3:swagger-annotations")

        // security
        kapt("io.micronaut.security:micronaut-security-annotations")
        implementation("io.micronaut.security:micronaut-security")
        implementation("io.micronaut.security:micronaut-security-jwt")
        implementation("io.micronaut.security:micronaut-security-oauth2")

        // kotlin
        implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
        implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // caching
        implementation("io.micronaut.cache:micronaut-cache-caffeine")

        // reactor
        implementation("io.micronaut.reactor:micronaut-reactor")
        implementation("io.micronaut.reactor:micronaut-reactor-http-client")

        // coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")

        // data
        kapt("io.micronaut.data:micronaut-data-processor")
        implementation("io.micronaut.data:micronaut-data-jdbc")
        implementation("io.micronaut.flyway:micronaut-flyway")
        implementation("io.micronaut.sql:micronaut-jdbc-hikari")
        implementation("jakarta.annotation:jakarta.annotation-api")
        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    }

    java {
        sourceCompatibility = JavaVersion.toVersion("18")
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                jvmTarget = "18"
            }
        }
        compileTestKotlin {
            kotlinOptions {
                jvmTarget = "18"
            }
        }
    }

    sonar {
        properties {
            property("sonar.sources", "src/main")
        }
    }


    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }
    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            csv.required.set(false)
        }
    }
    jacoco {
        toolVersion = "0.8.8"
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
    }
}


jacoco {
    toolVersion = "0.8.8"
}

sonar {
    properties {
        property("sonar.projectKey", "Black-Forrest-Development_open-event")
        property("sonar.organization", "black-forrest-development")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.core.codeCoveragePlugin", "jacoco")
    }
}


