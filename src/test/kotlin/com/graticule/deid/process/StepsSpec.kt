package com.graticule.deid.process

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.data.forall
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.tables.row
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom

class StepsSpec : StringSpec() {
    init {
        "First3LettersStep should return the first 3 letters or empty string and error" {
            forall(
                    row("bigstring", "big", 0),
                    row("biggerstring", "big", 0),
                    row("big", "big", 0),
                    row("bi", "", 1), //too short so return empty string and errors
                    row("", "", 1) //too short so return empty string and errors
            ) { stringIn, first3, errorsLength ->

                val stepResult = First3LettersStep.run(StepResult(stringIn), StepConfig("First3Letters"))

                stepResult.result shouldBe first3
                stepResult.errors.size shouldBe errorsLength
            }
        }

        "First3LettersStep should return the first 3 letters for random strings of length 3 or more" {
            val stringGen: Gen<String> = BoundedStringGen(3, 5)
            forAll(1000, stringGen) { stringIn: String ->
                val stepResult = First3LettersStep.run(StepResult(stringIn), StepConfig("First3Letters"))
                stepResult.result == stringIn.substring(0, 3)
            }
        }

        "First3LettersStep should return the first empty string and errors for random strings of length less than 3" {
            val stringGen: Gen<String> = BoundedStringGen(0, 2)
            forAll(1000, stringGen) { stringIn: String ->
                val stepResult = First3LettersStep.run(StepResult(stringIn), StepConfig("First3Letters"))
                stepResult.result == ""
                        && stepResult.errors.size == 1
            }
        }

        "ParseDateStep should return date normalized into yyyyMMdd or empty string and error" {
            forall(
                    row("1900-01-12", "19000112", 0)
            ) { stringIn, stringOut, errorsLength ->

                val stepResult = ParseDateStep.run(StepResult(stringIn), StepConfig("StartDateStep", mapOf("format" to "yyyy-MM-dd") ))

                stepResult.result shouldBe stringOut
                stepResult.errors.size shouldBe errorsLength
            }
        }

        "ParseDateStep should return date normalized into yyyyMMdd for random valid dates" {
            val localDateGen: Gen<LocalDate> = BoundedLocalDateGen(LocalDate.of(1800,1,1 ), LocalDate.of(9999, 12,31)) //Years are 4 digits
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            forAll(1000, localDateGen) { localDateIn->
                val stepResult = ParseDateStep.run(StepResult(inputFormatter.format(localDateIn)), StepConfig("StartDateStep", mapOf("format" to "yyyy-MM-dd") ))
                stepResult.result == inputFormatter.format(localDateIn).replace("-","")
                        && stepResult.errors.size == 0
            }
        }

    }

    class BoundedStringGen(val minLength: Int = 0, val maxLength: Int = 2000) : Gen<String> {
        override fun constants() = listOf<String>(" ".repeat(minLength), " ".repeat(maxLength))
        override fun random() = generateSequence { Gen.string().nextPrintableString(ThreadLocalRandom.current().nextInt(minLength, maxLength))}
    }

    class BoundedLocalDateGen(minLocalDate: LocalDate, maxLocalDate: LocalDate) : Gen<LocalDate> {
        val minDay = minLocalDate.toEpochDay()
        val maxDay = maxLocalDate.toEpochDay()
        override fun constants() = emptyList<LocalDate>()
        override fun random() = generateSequence {LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(minDay, maxDay)) }
    }
}
