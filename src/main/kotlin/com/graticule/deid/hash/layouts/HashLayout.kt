package com.graticule.deid.hash.layouts

import com.graticule.deid.hash.elements.ElementType
import com.graticule.deid.hash.elements.ElementType.*

enum class HashLayout(val layout: List<ElementType>) {
    FNLNDOB(listOf(FIRST_NAME, LAST_NAME, BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY)),
    FNLNBOD(listOf(FIRST_NAME, LAST_NAME, BIRTH_YEAR, BIRTH_DAY, BIRTH_MONTH)),
    LNFNDOB(listOf(LAST_NAME, FIRST_NAME, BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY))
}