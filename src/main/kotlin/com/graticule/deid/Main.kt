package com.graticule.deid

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.graticule.deid.hash.HashLayout
import com.graticule.deid.hash.Hasher
import com.graticule.deid.process.Processor
import com.graticule.deid.record.Record
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

//TODO warning that error files contain PII and must be safely handled
fun main(args: Array<String>) {
    println("Generating hashes for records...")

    //load and validate the config
    val configPath = Config::class.java.getResource("/config.yml").path
    val config = loadConfig(Paths.get(configPath))
    val configErrors = validateConfig(config)

    //read the file
    val format = CSVFormat.newFormat('^').withFirstRecordAsHeader()

    val inputFile = Config::class.java.getResource("/test-data.csv")
    val parser = CSVParser.parse(inputFile, StandardCharsets.UTF_8, format);

    parser.forEach {
        val record = Record(it.recordNumber.toString(), it.toMap())
        //println(record)
        val processor = Processor(config.mappings, config.pipelines)
        val results = processor.processRecord(record)

        //println("Mapping results $results")
        val hasher = Hasher("salt", config.layouts)
        val hashes = hasher.generateHashes(results)

        if (results.any { processResult -> !processResult.errors.isEmpty() }) {
            println("Record ${it.recordNumber} failed to produce all needed fields:")
            results.filter { result -> !result.errors.isEmpty() }.forEach {
                println("${it.mapping.target} : ${it.errors}")
            }
        }

        if (hashes.any { hashResult -> !hashResult.errors.isEmpty() }) {
            println("Record ${it.recordNumber} failed to produce all hashes:")
            hashes.filter { hash -> !hash.errors.isEmpty() }.forEach {
                println("${it.name} : ${it.errors}")
            }
        }

        hashes.forEach { println("Hash: ${it.name}:${it.value} errors: ${it.errors}") }
    }
}


//Config loading and validating TODO:Move me out of here
fun loadConfig(path: Path): Config {
    val mapper = ObjectMapper(YAMLFactory()) // Enable YAML parsing
    mapper.registerModule(KotlinModule()) // Enable Kotlin support

    return Files.newBufferedReader(path).use {
        mapper.readValue(it, Config::class.java)
    }
}

fun validateConfig(config: Config): List<ConfigError> {
    return validateLayouts(config.layouts) //+ validateMappings(config.mappings)
}

fun validateLayouts(layouts: List<HashLayout>): List<ConfigError> {

    fun validateUniqueLayoutNames(): List<ConfigError> {
        return layouts.groupingBy { it.name }.eachCount().filter { it.value > 1 }.map { ConfigError("Layout name ${it.key} is not unique") }
    }

    fun validateUniqueLayoutPatterns(): List<ConfigError> {
        return layouts.groupingBy { it.layout }.eachCount().filter { it.value > 1 }.map { ConfigError("Layout pattern ${it.key} is not unique") }
    }

    return validateUniqueLayoutNames() + validateUniqueLayoutPatterns()
}

