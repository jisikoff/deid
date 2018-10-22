package com.graticule.deid.hash

import com.graticule.deid.hash.elements.HashElement
import kotlin.reflect.KClass

data class HashLayout(val name:String, val elements: List<KClass<HashElement<*>>>)