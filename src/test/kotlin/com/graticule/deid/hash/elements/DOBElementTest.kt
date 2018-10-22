package com.graticule.deid.hash.elements

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DOBElementTest {

    @Test
    fun testDOBElement() {
        val dob = LocalDate.parse("1970-01-16")

        val dobElement = DOBElement(dob)

        assertEquals(dob, dobElement.value )
        assertEquals("1970-01-16", dobElement.valueString)
        assertEquals("DOBElement(value=1970-01-16)", dobElement.toString())
        assertEquals(DOBElement(dob), dobElement)
    }
}