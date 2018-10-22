package com.graticule.deid.hash.elements

abstract class HashElement(open val value: Any) {
    open val valueString:String
        get() = value.toString()
}