package com.graticule.deid.process

interface Step {
    abstract fun run(input:StepResult, step: StepConfig):StepResult
}