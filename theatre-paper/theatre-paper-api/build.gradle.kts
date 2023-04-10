plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.5.4"
    id("maven-publish")
}

group = "de.mcmdev"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":theatre-common:theatre-common-api"))
    implementation(project(":theatre-common:theatre-common-platform"))
    // compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn("shadowJar")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.mcmdev"
            artifactId = "theatre-paper-api"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}