package com.graticule.deid.hash.layouts

import com.graticule.deid.hash.elements.ElementType.*

class FNLNDOBLayout(): HashLayout("FNLNDOB", listOf(FIRST_NAME, LAST_NAME, DOB))