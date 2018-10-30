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
        when (step.name) {
            "strip" -> return stripStep(input, step)
            "first3letters" -> return first3LettersStep(input, step)
            "parseDate" -> return parseDateStep(input, step)
            "year" -> return yearStep(input, step)
            "month" -> return monthStep(input, step)
            "day" -> return dayStep(input, step)
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
        return StepResult(step, input.result, input.errors + StepError("$step.name step not found"))
    }

    fun first3LettersStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result.substring(0,3), input.errors)
    }

    fun parseDateStep(input:StepResult, step: Step):StepResult {
        val defaultFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        val formatOption = step.options.get("format")
        val inputFormatter =  if (formatOption == null)  defaultFormatter else  DateTimeFormatter.ofPattern(formatOption as String)

        val parsed = LocalDate.parse(input.result, inputFormatter)

        return StepResult(step, parsed.format(defaultFormatter), input.errors)
    }

    fun yearStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result.substring(0,4), input.errors)
    }

    fun monthStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result.substring(4,6), input.errors)
    }

    fun dayStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result.substring(6,8), input.errors)
    }

    fun stripStep(input:StepResult, step: Step):StepResult {
        val re = Regex("[^A-Za-z0-9 ]")
        return StepResult(step, re.replace(input.result, ""), input.errors)
    }
    fun lowercaseStep(input:StepResult, step: Step):StepResult {
        return StepResult(step, input.result.toLowerCase(), input.errors)
    }
    fun soundexStep(input:StepResult, step: Step):StepResult {
        val soundex = Soundex()
        return StepResult(step, soundex.encode(input.result), input.errors)
    }
}