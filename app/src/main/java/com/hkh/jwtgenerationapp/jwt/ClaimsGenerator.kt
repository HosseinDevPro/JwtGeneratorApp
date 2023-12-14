package com.hkh.jwtgenerationapp.jwt

object ClaimsGenerator {

    fun getSampleClaims(): Pair<String, String> {

        val currentTime = Utils.getCurrentTimeInSecond()

        val header = JwtHeaderBuilder().run {
            algorithm("ES256")
            type("JWT")
            contentType("application/json")
            keyId("UniqueKeyId")
        }.build()

        val payload = JwtPayloadBuilder().run {
            issuer("Hossein KheirollahPour")
            subject("OneClickPayment")
            audience("https://blubank.com")
            issuedAt(currentTime)
            notBefore(currentTime)
            expirationTime(currentTime + 3600)// Expires after 1 hour
            jwtID("UniqueJwtId1234")
        }.build()

        return Pair(header, payload)
    }

}