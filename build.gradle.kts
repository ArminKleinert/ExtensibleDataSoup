plugins {
    kotlin("jvm") version "1.9.24"
    application
    id("org.jetbrains.dokka") version "2.0.0"
}

group = "de.kleinert"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
    dokkaSourceSets {
        configureEach {
            samples.from("$projectDir/src/samples/kotlin/samples/samples.kt")
        }
    }
}