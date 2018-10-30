package com.graticule.deid.hash

import com.graticule.deid.process.MappingResult
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

//TODO salt per hash layout?
//TODO Encryption
//TODO hash algorithm which?
class Hasher(val salt: String, val layouts: List<HashLayout>) {

    fun generateHashes(mappingResults: List<MappingResult>):List<HashResult> {
        val elementMap:Map<ElementType, String> = mappingResults.filter {it.errors.isEmpty()}.associateBy ({it.mapping.target}, {it.result})

        val hashInputs = layouts.map{ layoutHashInput(it, elementMap)}
        hashInputs.forEach{ println("Hash inputs: ${it.name} - ${it.value} errors: ${it.errors}")}
        return hashInputs.map{ doHash(it)}
    }

    fun layoutHashInput(layout: HashLayout, elementMap: Map<ElementType, String>) :HashInput {

        val hashInput = layout.layout.fold(HashInput(layout.name, "")) { acc: HashInput, elementType: ElementType ->
            var errors = listOf<HashError>()

            if(!elementMap.containsKey(elementType))
                errors += HashError("Hash ${acc.name} requires missing elementType ${elementType}")

            val elementValue: String = elementMap.get(elementType) ?: ""

            HashInput(acc.name, acc.value + elementValue, acc.errors + errors)
        }

        return hashInput
    }

    fun doHash(hashInput: HashInput):HashResult {
        var hashResultValue = ""

        if(hashInput.errors.isEmpty()) {
            val md = MessageDigest.getInstance("SHA-512")
            md.update(salt.toByteArray(Charsets.UTF_8) + hashInput.value.toByteArray(Charsets.UTF_8))
            val aMessageDigest = md.digest()
            hashResultValue = Base64.getEncoder().encodeToString(aMessageDigest)
        }
        return (HashResult(hashInput.name, hashResultValue, hashInput.errors))
    }
}