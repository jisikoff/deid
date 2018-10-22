package com.graticule.deid.hash.elements

import org.junit.Assert.assertEquals
import org.junit.Test
class LastNameElementTest {

    @Test
    fun testLastNameElement() {
        val LastNameElement = LastNameElement("Last")

        assertEquals("Last", LastNameElement.value )
        assertEquals("Last", LastNameElement.valueString)
        assertEquals("LastNameElement(value=Last)", LastNameElement.toString())
        assertEquals(LastNameElement("Last"),LastNameElement)
    }
}