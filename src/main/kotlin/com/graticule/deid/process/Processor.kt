package com.graticule.deid.process

import com.graticule.deid.hash.ElementType
import com.graticule.deid.record.Record
import org.apache.commons.codec.language.Soundex

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
            "processName" -> return processNameStep(input, step)
            "strip" -> return stripStep(input, step)
            "parseDate" -> return parseDateStep(input, step)
            "lowercase" -> return lowercaseStep(input, step)
            "soundex" -> return soundexStep(input, step)
            "test" -> return testStep(input, step)
            else -> return  unknownStep(input, step)
        }
    }

    fun testStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result + step.name, input.errors + StepError(step.name))
    }
    fun unknownStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result + step.name, input.errors + StepError("$step.name step not found"))
    }

    fun processNameStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result, input.errors)
    }

    fun parseDateStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result + step.name, input.errors + StepError("$step.name step not found"))
    }

    fun stripStep(input:StepResult, step: Step):StepResult {
        val re = Regex("[^A-Za-z0-9 ]")
        return StepResult(step, re.replace(input.result, ""), input.errors)
    }
    fun lowercaseStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result.toLowerCase() + step.name, input.errors)
    }
    fun soundexStep(input:StepResult, step: Step):StepResult {
        val soundex = Soundex()
        return StepResult(step, soundex.encode(input.result), input.errors)
    }
}