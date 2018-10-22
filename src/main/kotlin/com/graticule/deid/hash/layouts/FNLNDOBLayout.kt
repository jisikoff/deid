package com.graticule.deid.hash.layouts

import com.graticule.deid.hash.elements.DOBElement
import com.graticule.deid.hash.elements.FirstNameElement
import com.graticule.deid.hash.elements.LastNameElement
import com.graticule.deid.hash.layouts.HashLayout

class FNLNDOBLayout(): HashLayout("FNLNDOB", FirstNameElement::class, LastNameElement::class, DOBElement::class)