package com.graticule.deid.process

import com.graticule.deid.DeidError

data class MappingResult(val mapping:Mapping, val result:String,val errors:List<DeidError>)