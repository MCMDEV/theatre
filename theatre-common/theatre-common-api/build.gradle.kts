plugins {
    id("maven-publish")
}

group = "de.mcmdev"
version = "1.0-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.mcmdev"
            artifactId = "theatre-common-api"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}