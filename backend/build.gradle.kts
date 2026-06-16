import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.allopen") version "2.4.0"
    kotlin("plugin.jpa") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"

    id("com.google.devtools.ksp") version "2.3.9"
    id("org.sonarqube") version "7.3.1.8318"
    id("net.researchgate.release") version "3.1.0"
    id("com.google.cloud.tools.jib") version "3.5.3"

    id("io.micronaut.application") version "5.0.0"
    id("io.micronaut.test-resources") version "5.0.0"
    id("io.micronaut.aot") version "5.0.0"

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


micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(false)
        annotations("de.sambalmueslie.openevent.*")
    }
    aot {
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
        configurationProperties.put("micronaut.security.jwks.enabled", "false")
        configurationProperties.put("micronaut.security.openid-configuration.enabled", "false")
    }
}


dependencies {
    implementation("ch.qos.logback:logback-classic:1.5.34")
    runtimeOnly("org.yaml:snakeyaml")

    testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.1.0")
    testImplementation("io.mockk:mockk:1.14.11")

    // jackson
    ksp("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut:micronaut-jackson-databind")
//    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // http
    implementation("io.micronaut:micronaut-http-client")

    // validation
    implementation("jakarta.validation:jakarta.validation-api")
    ksp("io.micronaut.validation:micronaut-validation-processor")
    implementation("io.micronaut.validation:micronaut-validation")

    // openapi
    ksp("io.micronaut.openapi:micronaut-openapi")
    implementation("io.swagger.core.v3:swagger-annotations")

    // security
    ksp("io.micronaut.security:micronaut-security-annotations")
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.security:micronaut-security-oauth2")
    aotPlugins("io.micronaut.security:micronaut-security-aot:5.0.0")

    // kotlin
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.4.0")

    // caching
//    implementation("io.micronaut.cache:micronaut-cache-caffeine")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.4")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.11.0")
    // reactor
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.reactor:micronaut-reactor-http-client")
    // data
    ksp("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.flyway:micronaut-flyway")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    runtimeOnly("org.postgresql:postgresql")

    // velocity
    implementation("org.apache.velocity:velocity-engine-core:2.4.1")
    implementation("org.apache.velocity.tools:velocity-tools-generic:3.1")

    // OpenHTMLtoPDF
    implementation("com.openhtmltopdf:openhtmltopdf-pdfbox:1.0.10")
    implementation("com.openhtmltopdf:openhtmltopdf-svg-support:1.0.10")

    // qrcode
    implementation("com.google.zxing:core:3.5.4")
    implementation("com.google.zxing:javase:3.5.4")

    // POI
    implementation("org.apache.poi:poi:5.5.1")
    implementation("org.apache.poi:poi-ooxml:5.5.1")
    implementation("builders.dsl:spreadsheet-builder-poi:4.0.1")

    // mail
    implementation("org.simplejavamail:simple-java-mail:8.12.6")
    implementation("org.simplejavamail:batch-module:8.12.6")
    implementation("org.simplejavamail:authenticated-socks-module:8.12.6")

    // test
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.opensearch:opensearch-testcontainers:4.1.0")
    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
    testImplementation("io.fusionauth:fusionauth-jwt:6.0.0")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")

    // tracing
    implementation("io.micronaut.tracing:micronaut-tracing-jaeger")
    // opensearch
    implementation("com.jillesvangurp:search-client:2.8.7")

    // jsoup
    implementation("org.jsoup:jsoup:1.22.2")
    // biweekly
    implementation("net.sf.biweekly:biweekly:0.6.8")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
}


graalvmNative.toolchainDetection = false

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "25"
}


tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
    }

    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
    }
}



tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "-XX:+UnlockExperimentalVMOptions",
        "-XX:+UseParallelGC"
    )
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
    toolVersion = "0.8.15"
}

tasks.named("internalStartTestResourcesService") {
    setProperty("useClassDataSharing", false)
}

sonar {
    properties {
        property("sonar.projectKey", "Black-Forest-Interactive_open-event")
        property("sonar.organization", "black-forest-interactive")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.core.codeCoveragePlugin", "jacoco")
        property("sonar.sources", "src/main")
    }
}

application {
    mainClass.set("de.sambalmueslie.openevent.OpenEventApplication")
}

jib {
    from.image = "eclipse-temurin:25-jre-alpine"
    to {
        image = "open-event-backend"
        tags = setOf(version.toString(), "latest")
    }
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
        mainClass = application.mainClass.get()
        jvmFlags = listOf(
            "-server",
            "-XX:+UseContainerSupport",
            "-XX:MaxRAMPercentage=75.0",
            "-XX:+UseZGC",
            "-XX:ZCollectionInterval=5",
            "-XX:ZUncommitDelay=300",
            "-XX:+TieredCompilation",
            "-XX:TieredStopAtLevel=1",  // Fast startup for microservices
            "-XX:+UseStringDeduplication",
            "-XX:+OptimizeStringConcat",
            "-Dmicronaut.runtime.environment=prod",
            "-Dio.netty.allocator.maxOrder=3",
            "-Dio.netty.leakDetection.level=disabled"  // Production setting
        )

        user = "1001"

        environment = mapOf(
            "JAVA_TOOL_OPTIONS" to "-XX:+ExitOnOutOfMemoryError",
            "MALLOC_ARENA_MAX" to "2"  // Reduce memory fragmentation
        )
    }
}


release {
    git {
        requireBranch.set("development")
    }
    pushReleaseVersionBranch.set("master")
}
