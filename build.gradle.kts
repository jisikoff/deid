import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("org.apache.commons:commons-csv:1.6")
    implementation("commons-codec:commons-codec:1.11")
    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    testCompile ("io.kotlintest:kotlintest-runner-junit5:3.1.8")
    testCompile ("com.nhaarman:mockito-kotlin:1.6.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

application {
    mainClassName = "com.graticule.deid.MainKt"
}

tasks.withType<ShadowJar> {
    baseName = "deid"
    classifier = ""
    version = ""
}