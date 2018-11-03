package com.graticule.deid.hash

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import com.graticule.deid.hash.ElementType.*

class HashLayoutSpec: StringSpec() {
    init {
        "hash layouts should equal each other if contain same name and elements" {
            val hashLayout = HashLayout("FNLNDOB", listOf(FIRST_NAME, LAST_NAME))

            HashLayout("FNLNDOB", listOf(FIRST_NAME, LAST_NAME)) shouldBe hashLayout
        }
    }
}