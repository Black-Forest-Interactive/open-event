pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.github.johnrengelman.shadow") {
                useModule("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.0.0")
            }
        }
    }
}

rootProject.name = "open-event"

include("backend")
include("frontend")