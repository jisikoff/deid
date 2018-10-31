package com.graticule.deid.process

import com.graticule.deid.hash.ElementType
import com.graticule.deid.record.Record
import org.apache.commons.codec.language.Soundex
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Processor(val mappings: List<Mapping>, pipelines: List<Pipeline>) {

    val pipelinesMap = pipelines.associateBy({ it.name }, { it })


    fun processRecord(record: Record):List<MappingResult> {
      val fieldMap:Map<String, String> = record.fields.associateBy ({it.name}, {it.value})

        fun doMapping(mapping: Mapping):MappingResult{
            var errors = listOf<MappingError>()

            if(!fieldMap.containsKey(mapping.source))
                errors += MappingError("${mapping.source} not found in record ${record.id}")

            val sourceValue: String = fieldMap.get(mapping.source) ?: ""

            if(!pipelinesMap.containsKey(mapping.pipeline))
                throw Exception("Pipeline ${mapping.pipeline} not found")

            val steps = pipelinesMap.getValue(mapping.pipeline).steps

            val startStep = StepResult(Step("start"), sourceValue)
            val finalResult = steps.fold(startStep, ::doStep)
            return MappingResult(mapping, finalResult.result, finalResult.errors + errors)
        }

        val mappings =  mappings.map{ doMapping(it) }
        mappings.forEach{println(it)}
        return mappings
    }

    fun doStep(input:StepResult, step: Step):StepResult {
        val stepFunction = Steps.lookupStepFunction(step.name)
        return stepFunction(input, step)
    }
}