plugins {
    `java-library`
}

group = "com.frostclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

repositories {
    maven {
        url = uri("https://pkg.frst.cloud/releases")
        credentials {
            username = System.getenv("alias") ?: ""
            password = System.getenv("token") ?: ""
        }
        authentication {
            create<BasicAuthentication>("basic")
        }
    }
}

