import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val deid_version: String by project

val bouncy_castle_version: String by project

plugins {
    application
    kotlin("jvm") version "1.2.71"
    id("com.github.johnrengelman.shadow") version "4.0.1"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("org.bouncycastle:bcprov-jdk15on:1.60")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.7")
    implementation( "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    implementation( "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.7")

	testCompile("org.jetbrains.kotlin:kotlin-test")
	testCompile("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "com.graticule.deid.MainKt"
}

tasks.withType<ShadowJar> {
    baseName = "deid"
    classifier = ""
    version = ""
}