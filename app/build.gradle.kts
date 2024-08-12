plugins {
    id("java")
    application
}

group = "io.github.gmodena"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

dependencies {
    implementation(project(":index"))
    implementation("org.openjdk.jmh:jmh-core:1.36")
    implementation("commons-cli:commons-cli:1.4")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("io.github.gmodena.wikinews.Runner")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test> {
    jvmArgs("--enable-preview")
}

tasks.withType<JavaExec> {
    jvmArgs("--enable-preview")
}