package com.hkh.jwtgenerationapp.jwt

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
object Base64Util {

    fun base64UrlEncode(input: ByteArray): String {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input)
    }
    fun base64UrlDecode(input: String): ByteArray {
        return Base64.getUrlDecoder().decode(input)
    }

    fun base64Encode(input: ByteArray): String {
        return Base64.getEncoder().encodeToString(input)
    }
    fun base64Decode(input: String): ByteArray {
        return Base64.getDecoder().decode(input)
    }

}