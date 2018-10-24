package com.graticule.deid.process

import com.graticule.deid.DeidError

data class StepResult(val step:Step, val result:String, val errors:List<StepError> = listOf())