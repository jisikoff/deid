package com.graticule.deid.hash

import com.graticule.deid.hash.elements.FirstNameElement
import com.graticule.deid.hash.elements.LastNameElement
import org.junit.Assert.assertEquals
import org.junit.Test
class HashLayoutTest {

    @Test
    fun testHashLayout() {
        val hashLayout = HashLayout("FNLNDOB", listOf(FirstNameElement::class, LastNameElement::class))

        assertEquals("Jeremy", firstNameElement.value )
        assertEquals("Jeremy", firstNameElement.valueString)
        assertEquals("FirstNameElement(value=Jeremy)", firstNameElement.toString())
        assertEquals(FirstNameElement("Jeremy"),firstNameElement)
    }
}