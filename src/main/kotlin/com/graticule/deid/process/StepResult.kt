package com.graticule.deid.process

data class StepResult(val result: String, val errors: List<StepError> = listOf())