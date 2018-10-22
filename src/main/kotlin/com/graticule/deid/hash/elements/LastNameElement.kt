package com.graticule.deid.hash.elements

import com.graticule.deid.hash.elements.HashElement

data class LastNameElement(override val value:String): HashElement<String>(value)