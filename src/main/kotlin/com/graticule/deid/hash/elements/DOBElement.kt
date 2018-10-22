package com.graticule.deid.hash.elements

import java.time.LocalDate

data class DOBElement(override val value:LocalDate): HashElement(value)