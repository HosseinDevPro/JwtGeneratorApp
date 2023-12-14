package com.hkh.jwtgenerationapp.jwt

import org.json.JSONObject

class JwtPayloadBuilder {
    private val payload = JSONObject()

    fun issuer(iss: String) = apply { payload.put(Constants.ISSUER, iss) }
    fun subject(sub: String) = apply { payload.put(Constants.SUBJECT, sub) }
    fun audience(aud: String) = apply { payload.put(Constants.AUDIENCE, aud) }
    fun issuedAt(iat: Long) = apply { payload.put(Constants.ISSUED_AT, iat) }
    fun notBefore(nbf: Long) = apply { payload.put(Constants.NOT_BEFORE, nbf) }
    fun expirationTime(exp: Long) = apply { payload.put(Constants.EXPIRES_AT, exp) }
    fun jwtID(jti: String) = apply { payload.put(Constants.JWT_ID, jti) }

    fun build(): String = payload.toString(4) // Using 4 Space For Pretty Json String Format
}