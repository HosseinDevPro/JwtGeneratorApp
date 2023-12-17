package com.hkh.jwtgeneratorapp.jwt

import org.json.JSONObject

class JwtHeaderBuilder {
    private val header = JSONObject()

    fun algorithm(alg: String) = apply { header.put(Constants.ALGORITHM, alg) }
    fun type(typ: String) = apply { header.put(Constants.TYPE, typ) }
    fun contentType(cty: String) = apply { header.put(Constants.CONTENT_TYPE, cty) }
    fun keyId(kid: String) = apply { header.put(Constants.KEY_ID, kid) }

    fun build(): String = header.toString(4) // Using 4 Space For Pretty Json String Format
}