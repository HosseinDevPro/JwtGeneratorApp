package com.hkh.jwtgeneratorapp.jwt

import android.os.Build
import androidx.annotation.RequiresApi
import com.hkh.jwtgeneratorapp.jwt.Base64Util.base64UrlDecode
import com.hkh.jwtgeneratorapp.jwt.Base64Util.base64UrlEncode
import com.hkh.jwtgeneratorapp.jwt.Utils.toStandardByteArray
import java.security.Signature
import java.security.SignatureException
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import kotlin.math.max
import kotlin.math.min

object JwtUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun generate(privateKey: ECPrivateKey, headerJson: String, payloadJson: String): String {
        val header = base64UrlEncode(headerJson.toStandardByteArray())
        val payload = base64UrlEncode(payloadJson.toStandardByteArray())
        val contentBytes = prepareAggregatedContentBytes(header.toStandardByteArray(), payload.toStandardByteArray())

        val signatureBytes = generateSignature(privateKey, contentBytes)

        val convertedSignatureBytes = DERToJOSE(signatureBytes)

        val encodedSignature = base64UrlEncode(convertedSignatureBytes)

        return String.format("%s.%s.%s", header, payload, encodedSignature)
    }

    private fun prepareAggregatedContentBytes(headerBytes: ByteArray, payloadBytes: ByteArray): ByteArray {
        val contentBytes = ByteArray(headerBytes.size + 1 + payloadBytes.size)
        System.arraycopy(headerBytes, 0, contentBytes, 0, headerBytes.size)
        contentBytes[headerBytes.size] = '.'.code.toByte()
        System.arraycopy(payloadBytes, 0, contentBytes, headerBytes.size + 1, payloadBytes.size)
        return contentBytes
    }

    private fun generateSignature(privateKey: ECPrivateKey, contentBytes: ByteArray): ByteArray {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(privateKey)
        signature.update(contentBytes)
        return signature.sign()
    }

    @Throws(SignatureException::class)
    private fun DERToJOSE(derSignature: ByteArray): ByteArray {
        val ecNumberSize = 32
        val derEncoded = derSignature[0].toInt() == 0x30 && derSignature.size != ecNumberSize * 2
        if (!derEncoded) {
            throw SignatureException("Invalid DER signature format.")
        }
        val joseSignature = ByteArray(ecNumberSize * 2)
        var offset = 1
        if (derSignature[1] == 0x81.toByte()) {
            offset++
        }
        val encodedLength = derSignature[offset++].toInt() and 0xff
        if (encodedLength != derSignature.size - offset) {
            throw SignatureException("Invalid DER signature format.")
        }
        offset++
        val rLength = derSignature[offset++].toInt()
        if (rLength > ecNumberSize + 1) {
            throw SignatureException("Invalid DER signature format.")
        }
        val rPadding: Int = ecNumberSize - rLength
        System.arraycopy(
            derSignature,
            offset + max(-rPadding, 0),
            joseSignature,
            max(rPadding, 0),
            rLength + min(rPadding, 0)
        )
        offset += rLength + 1
        val sLength = derSignature[offset++].toInt()
        if (sLength > ecNumberSize + 1) {
            throw SignatureException("Invalid DER signature format.")
        }
        val sPadding: Int = ecNumberSize - sLength
        System.arraycopy(
            derSignature,
            offset + max(-sPadding, 0),
            joseSignature,
            ecNumberSize + max(sPadding, 0),
            sLength + min(sPadding, 0)
        )
        return joseSignature
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun manualVerifyJWT(publicKey: ECPublicKey, jwt: String): Boolean {
        val parts = jwt.split(".")
        if (parts.size != 3) return false
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update("${parts[0]}.${parts[1]}".toByteArray())
        return signature.verify(base64UrlDecode(parts[2]))
    }

}