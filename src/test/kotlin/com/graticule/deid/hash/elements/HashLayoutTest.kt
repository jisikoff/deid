package com.graticule.deid.hash

import com.graticule.deid.hash.elements.FirstNameElement
import com.graticule.deid.hash.elements.LastNameElement
import com.graticule.deid.hash.layouts.HashLayout
import org.junit.Assert.assertEquals
import org.junit.Test
class HashLayoutTest {

    @Test
    fun testHashLayout() {
        val hashLayout = HashLayout("FNLNDOB", listOf(FirstNameElement::class, LastNameElement::class))


        assertEquals(HashLayout("FNLNDOB", listOf(FirstNameElement::class, LastNameElement::class)), hashLayout)
    }
}