package com.graticule.deid.process

import org.apache.commons.codec.language.Soundex
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
        return StepResult(step, input.result.substring(0, 3), input.errors)
    }
}

object ParseDateStep:Step {
    val defaultFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    override fun run(input: StepResult, step: StepConfig): StepResult {

        val formatOption = step.options.get("format")
        val inputFormatter = if (formatOption == null) defaultFormatter else DateTimeFormatter.ofPattern(formatOption as String)

        val parsed = LocalDate.parse(input.result, inputFormatter)

        return StepResult(step, parsed.format(defaultFormatter), input.errors)
    }
}

object YearStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        return StepResult(step, input.result.substring(0, 4), input.errors)
    }
}

object MonthStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        return StepResult(step, input.result.substring(4, 6), input.errors)
    }
}

object DayStep:Step {
    override fun run(input: StepResult, step: StepConfig): StepResult {
        return StepResult(step, input.result.substring(6, 8), input.errors)
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