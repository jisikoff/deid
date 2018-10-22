package com.graticule.deid.hash.elements

abstract class HashElement<T>(open val value: T) {
    open val valueString:String
        get() = value.toString()
}