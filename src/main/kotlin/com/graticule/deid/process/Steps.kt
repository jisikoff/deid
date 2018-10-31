package com.graticule.deid.process

import org.apache.commons.codec.language.Soundex
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class Steps {
    companion object {
        fun lookupStepFunction(name: String):(StepResult, Step) -> StepResult {
           return when (name) {
                "strip" -> return ::stripStep
                "first3letters" -> return ::first3LettersStep
                "parseDate" -> return ::parseDateStep
                "year" -> return ::yearStep
                "month" -> return ::monthStep
                "day" -> return ::dayStep
                "lowercase" -> ::lowercaseStep
                "soundex" -> ::soundexStep
                "test" -> return ::testStep
                else -> return  ::unknownStep
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
}