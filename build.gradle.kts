plugins {
    `java-library`
    `maven-publish`
}

group = "com.frostclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://pkg.frst.cloud/releases")
            credentials {
                username = System.getenv("ALIAS") ?: ""
                password = System.getenv("TOKEN") ?: ""
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}
