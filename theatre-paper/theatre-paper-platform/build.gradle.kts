plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.mcmdev"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":theatre-common:theatre-common-api"))
    implementation(project(":theatre-common:theatre-common-platform"))

    implementation(project(":theatre-paper:theatre-paper-api"))
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn("shadowJar")
    }
}