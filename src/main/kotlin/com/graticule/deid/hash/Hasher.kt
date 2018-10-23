package com.graticule.deid.hash

import com.graticule.deid.hash.elements.HashElement
import com.graticule.deid.hash.layouts.HashLayout
import java.lang.annotation.ElementType

class Hasher(val seed: String) {

    fun generateHashes(elements: List<HashElement>):List<HashResult> {
        return HashLayout.values()
                .map{  it -> applyLayout(it.layout, elements)}
    }
}