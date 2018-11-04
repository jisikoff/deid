package com.graticule.deid

import com.graticule.deid.hash.HashLayout
import com.graticule.deid.process.Mapping
import com.graticule.deid.process.Pipeline

data class Config(val layouts: List<HashLayout>, val pipelines: List<Pipeline>, val mappings: List<Mapping>) {

    val layoutMap: Map<String, HashLayout> = layouts.associateBy({ it.name }, { it })
    val pipelineMap: Map<String, Pipeline> = pipelines.associateBy({ it.name }, { it })

}