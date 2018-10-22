package com.graticule.deid.hash.layouts

import com.graticule.deid.hash.elements.ElementType.*

class FNLNBODLayout(): HashLayout("FNLNDOB", listOf(FIRST_NAME, LAST_NAME, BIRTH_YEAR, BIRTH_DAY, BIRTH_MONTH))