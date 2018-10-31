package com.graticule.deid

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.graticule.deid.hash.HashLayout
import com.graticule.deid.hash.Hasher
import com.graticule.deid.process.Processor
import com.graticule.deid.record.Record
import com.graticule.deid.record.RecordField
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

fun main(args : Array<String>) {
    println("Hello, world! KTS")
    val configPath = Config::class.java.getResource("/config.yml").path
    val config = loadConfig( Paths.get(configPath))
    val configErrors = validateConfig(config)

    val field = RecordField("first_name_field", "jeremy")
    val field2 = RecordField("last_name_field", "lastly")
    val field3 = RecordField("social_security_number_field", "666-66-6666")
    val field4 = RecordField("dob", "1971-05-29")
    val record = Record("1", listOf(field, field2, field3, field4))
    val processor = Processor(config.mappings, config.pipelines)
    val results = processor.processRecord(record)


    println("Mapping results $results")
    val hasher = Hasher("salt", config.layouts)
    val hashes = hasher.generateHashes(results)

    val time = measureTimeMillis {
        repeat(100000) {
            hasher.generateHashes(results)
        }
    }
    hashes.forEach { println("Hash: ${it.name}:${it.value} errors: ${it.errors}") }
    println("time $time")
}

fun loadConfig(path: Path): Config {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.registerModule(KotlinModule()) // Enable Kotlin support

    return Files.newBufferedReader(path).use {
        mapper.readValue(it, Config::class.java)
    }
}

fun validateConfig(config: Config):List<ConfigError> {
    return validateLayouts(config.layouts) //+ validateMappings(config.mappings)
}

fun validateLayouts(layouts: List<HashLayout>):List<ConfigError> {

    fun validateUniqueLayoutNames():List<ConfigError>{
        return layouts.groupingBy { it.name }.eachCount().filter { it.value > 1 }.map { ConfigError("Layout name ${it.key} is not unique")}
    }

    fun validateUniqueLayoutPatterns():List<ConfigError>{
        return layouts.groupingBy { it.layout }.eachCount().filter { it.value > 1 }.map { ConfigError("Layout pattern ${it.key} is not unique")}
    }

    return validateUniqueLayoutNames() + validateUniqueLayoutPatterns()
}

