package com.hkh.jwtgeneratorapp.jwt

import java.nio.charset.StandardCharsets

object Utils {

    fun String.toStandardByteArray() = toByteArray(StandardCharsets.UTF_8)

    fun getCurrentTimeInSecond() = System.currentTimeMillis() / 1000

}