package com.graticule.deid.process

data class StepResult(val step:StepConfig, val result:String, val errors:List<StepError> = listOf())