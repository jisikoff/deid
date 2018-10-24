package com.graticule.deid


open class DeidError(val message:String) {

    override fun toString():String {
        return "${this.javaClass.simpleName} - ${message}"
    }
}