package com.graticule.deid.hash.elements

import org.junit.Assert.assertEquals
import org.junit.Test
class FirstNameElementTest {

    @Test
    fun testFirstNameElement() {
        val firstNameElement = FirstNameElement("Jeremy")

        assertEquals("Jeremy", firstNameElement.value )
        assertEquals("Jeremy", firstNameElement.valueString)
        assertEquals("FirstNameElement(value=Jeremy)", firstNameElement.toString())
        assertEquals(FirstNameElement("Jeremy"),firstNameElement)
    }
}