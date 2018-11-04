package com.graticule.deid.process

import com.graticule.deid.hash.ElementType


data class Mapping(val source: String, val pipeline: String, val target: ElementType) {
}