package com.graticule.deid.hash

import com.graticule.deid.DeidError

data class HashInput(val name: String, val value: String, val errors: List<DeidError> = listOf())