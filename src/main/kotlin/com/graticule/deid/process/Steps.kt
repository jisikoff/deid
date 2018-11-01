package com.graticule.deid.process

import org.apache.commons.codec.language.Soundex
import java.lang.Exception
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class Steps {
    companion object {
        val stepMap = hashMapOf(
                "strip" to StripStep,
                "first3Letters" to First3LettersStep,
                "parseDate" to ParseDateStep,
                "year" to YearStep,
                "month" to MonthStep,
                "day" to DayStep,
                "lowercase" to LowercaseStep,
                "soundex" to SoundexStep,
                "test" to TestStep,
                "unknown" to UnknownStep)

        fun lookup(name: String):Step {
            return stepMap.getOrDefault(name, "unknown") as Step
        }
    }
}

object TestStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        return StepResult(step, input.result + step.name, input.errors + StepError(step.name))
    }
}

object UnknownStep:Step {
    override fun run(input:StepResult, step: StepConfig):StepResult {
        return StepResult(step, input.result, input.errors + StepError("$step.name step not found"))
    }
}

object First3LettersStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        var errors = listOf<StepError>()
        val first3 = if(input.result.length < 3) {
            errors += StepError("String value ${input.result} too short to get 3 letters from")
            ""
        } else input.result.substring(0, 3)
        return StepResult(step, first3, input.errors + errors)
    }
}

object ParseDateStep : Step {
    val defaultFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    override fun run(input: StepResult, step: StepConfig): StepResult {

        var errors = listOf<StepError>()
        val formatOption = step.options.get("format")
        val inputFormatter = if (formatOption == null) defaultFormatter else DateTimeFormatter.ofPattern(formatOption as String)

        val formatted = try {
            LocalDate.parse(input.result, inputFormatter).format(defaultFormatter)
        } catch (ex: DateTimeException) {
            errors += StepError("LocalDate.parse failed: ${ex.message}")
            "" //bad date
        }
        return StepResult(step, formatted, input.errors + errors)
    }
}

object YearStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        var errors = listOf<StepError>()
        val year = if(input.result.length < 4) {
            errors += StepError("Date value ${input.result} too short to get a valid year from")
            ""
        } else input.result.substring(0, 4)
        return StepResult(step, year, input.errors + errors)
    }
}

object MonthStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        var errors = listOf<StepError>()
        val month = if(input.result.length < 6) {
            errors += StepError("Date value ${input.result} too short to get a valid month from")
            ""
        } else input.result.substring(4, 6)
        return StepResult(step, month, input.errors + errors)
    }
}

object DayStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        var errors = listOf<StepError>()
        val day = if(input.result.length < 8) {
            errors += StepError("Value ${input.result} too short to get a valid day from")
            ""
        } else input.result.substring(6, 8)
        return StepResult(step, day, input.errors + errors)
    }
}

object StripStep:Step {
    val re = Regex("[^A-Za-z0-9 ]")
    override fun run(input: StepResult, step: StepConfig): StepResult {
        return StepResult(step, re.replace(input.result, ""), input.errors)
    }
}

object LowercaseStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        return StepResult(step, input.result.toLowerCase(), input.errors)
    }
}

object SoundexStep:Step {
    val soundex = Soundex()
    override fun run(input: StepResult, step: StepConfig): StepResult {
        return StepResult(step, soundex.encode(input.result), input.errors)
    }
}