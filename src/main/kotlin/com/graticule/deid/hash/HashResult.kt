package com.graticule.deid.hash

import com.graticule.deid.DeidError

data class HashResult(val name: String, val value: String, val errors: List<DeidError> = listOf())