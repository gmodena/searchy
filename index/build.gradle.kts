plugins {
    id("java")
    id("com.github.spotbugs") version "6.0.19"
    id("maven-publish")
    checkstyle
    pmd
}

group = "io.github.gmodena.searchy"
version = "0.4.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = file("$buildDir/reports/spotbugs.html")
        setStylesheet("fancy-hist.xsl")
    }
}

tasks.javadoc {
    source = sourceSets["main"].allJava // Specify the source files for which Javadoc will be generated

    // Set additional Javadoc options if needed
    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8" // Set character encoding
        charSet = "UTF-8"  // Set charset
        isAuthor = true    // Include author information
        isVersion = true   // Include version information
        links("https://docs.oracle.com/en/java/javase/21/docs/api/") // Add links to the JDK documentation
        addStringOption("Xdoclint:none", "-quiet")
    }
}

spotbugs {
    excludeFilter = file("config/spotbugs/spotbugs-exclude.xml")
}

checkstyle {
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = true
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/gmodena/searchy")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
