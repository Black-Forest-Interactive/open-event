plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.allopen")
    id("org.sonarqube")
    id("com.google.cloud.tools.jib") version "3.3.0"
    id("io.micronaut.application") version "3.6.2"
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("de.sambalmueslie.openevent.user.*")
    }
}


dependencies {
    implementation(project(":common"))
    // keycloak
    implementation("org.keycloak:keycloak-common:19.0.3")
    implementation("org.keycloak:keycloak-core:19.0.3")

    // database
    kapt("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    runtimeOnly("org.postgresql:postgresql")
    implementation("io.micronaut.flyway:micronaut-flyway")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")
}

application {
    mainClass.set("de.sambalmueslie.openevent.user.UserApplication")
}

