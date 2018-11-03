package com.graticule.deid.process

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import com.graticule.deid.hash.ElementType.*
import io.kotlintest.data.forall
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table

class StepsSpec: StringSpec() {
    init {
        "First3LettersStep should return the first 3 letters" {
            table(
                    headers("stringIn", "first3", "errorsLength"),
                    row("bigstring", "big", 0),
                    row("biggerstring", "big", 0),
                    row("big", "big", 0),
                    row("bi", "", 1), //too short so return empty string and errors
                    row("", "", 1) //too short so return empty string and errors
            ).forAll { stringIn, first3, errorsLength ->

                val stepResult = First3LettersStep.run(StepResult(stringIn), StepConfig("First3Letters"))

                stepResult.result shouldBe first3
                stepResult.errors.size shouldBe errorsLength
            }
        }
    }
}