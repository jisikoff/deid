package com.graticule.deid.hash.layouts

import com.graticule.deid.hash.elements.HashElement
import kotlin.reflect.KClass

open class HashLayout(val name:String, vararg val elements: KClass<out HashElement>) {

}