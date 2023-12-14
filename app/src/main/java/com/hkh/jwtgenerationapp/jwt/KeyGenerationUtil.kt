package com.hkh.jwtgenerationapp.jwt

import android.os.Build
import androidx.annotation.RequiresApi
import com.hkh.jwtgenerationapp.jwt.Base64Util.base64Encode
import java.security.KeyPairGenerator
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec

object KeyGenerationUtil {

    fun generateECKeyPair(): Pair<ECPublicKey, ECPrivateKey> {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ECGenParameterSpec("secp256r1"))
        val keyPair = keyPairGenerator.generateKeyPair()
        return keyPair.public as ECPublicKey to keyPair.private as ECPrivateKey
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertECPublicKeyToPEM(ecPublicKey: ECPublicKey): String {
        val base64Encoded = base64Encode(ecPublicKey.encoded)
        return buildString {
            append("-----BEGIN PUBLIC KEY-----\n")
            append(base64Encoded.chunked(64).joinToString("\n"))
            append("\n-----END PUBLIC KEY-----\n")
        }
    }

}