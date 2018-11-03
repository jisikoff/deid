package com.graticule.deid.process

import com.graticule.deid.record.Record

class Processor(val mappings: List<Mapping>, pipelines: List<Pipeline>) {

    val pipelinesMap = pipelines.associateBy({ it.name }, { it })


    fun processRecord(record: Record):List<MappingResult> {
      val fieldMap = record.fieldMap

        fun doMapping(mapping: Mapping):MappingResult{
            var errors = listOf<MappingError>()

            if(!fieldMap.containsKey(mapping.source))
                errors += MappingError("${mapping.source} not found in record ${record.id}")

            val sourceValue: String = fieldMap.get(mapping.source) ?: ""

            if(!pipelinesMap.containsKey(mapping.pipeline))
                throw Exception("Pipeline ${mapping.pipeline} not found")

            val stepConfigs = pipelinesMap.getValue(mapping.pipeline).steps

            val startValue = StepResult(sourceValue)
            val finalResult = stepConfigs.fold(startValue, ::doStep)
            return MappingResult(mapping, finalResult.result, finalResult.errors + errors)
        }

        val mappings =  mappings.map{ doMapping(it) }
        //mappings.forEach{println(it)}
        return mappings
    }

    fun doStep(input:StepResult, stepConfig: StepConfig):StepResult {
        val step = Steps.lookup(stepConfig.name)
        return step.run(input, stepConfig)
    }
}