package com.graticule.deid.hash.elements

import org.junit.Assert.assertEquals
import org.junit.Test
class SSNElementTest {

    @Test
    fun testSSNElement() {
        val SSNElement = SSNElement("666676666")

        assertEquals("666676666", SSNElement.value )
        assertEquals("666676666", SSNElement.valueString)
        assertEquals("SSNElement(value=666676666)", SSNElement.toString())
        assertEquals(SSNElement("666676666"),SSNElement)
    }
}