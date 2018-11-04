package com.graticule.deid.process

interface Step {
    abstract fun run(input: StepResult, stepConfig: StepConfig): StepResult
}