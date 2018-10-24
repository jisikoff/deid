package com.graticule.deid.process

import com.graticule.deid.hash.ElementType
import com.graticule.deid.hash.HashElement
import com.graticule.deid.record.Record

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

        return mappings.map{ doMapping(it) }
    }

    fun doStep(input:StepResult, step: Step):StepResult {
        when (step.name) {
            "processName" -> return testStep(input, step)
            "strip" -> return testStep(input, step)
            "parseDate" -> return testStep(input, step)
            "lowercase" -> return testStep(input, step)
            "soundex" -> return testStep(input, step)
            else -> return testStep(input, step)
        }
    }

    fun testStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result + step.name, input.errors + StepError(step.name))
    }

}