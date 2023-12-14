package com.hkh.jwtgenerationapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hkh.jwtgenerationapp.databinding.ActivityMainBinding
import com.hkh.jwtgenerationapp.jwt.ClaimsGenerator
import com.hkh.jwtgenerationapp.jwt.JwtUtil
import com.hkh.jwtgenerationapp.jwt.KeyGenerationUtil

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val (publicKey, privateKey) = KeyGenerationUtil.generateECKeyPair()
        val pkDer = KeyGenerationUtil.convertECPublicKeyToPEM(publicKey)
        Log.d("HKH", "PublicKeyDERFormat is: \n'$pkDer'")
        binding.publicKeyTextView.text = "PublicKey: \n" + pkDer

        val (headerJson, payloadJson) = ClaimsGenerator.getSampleClaims()
        Log.d("HKH", "HeaderJson: '${headerJson}'")
        Log.d("HKH", "PayloadJson: '${payloadJson}'")
        binding.headerTextView.text = "HeaderJson: \n" + headerJson
        binding.payloadTextView.text = "PayloadJson: \n" + payloadJson

        val jwt = JwtUtil.generate(privateKey, headerJson, payloadJson)
        Log.d("HKH", "Generated JWT: '$jwt'")
        binding.jwtTextView.text = "JWT: \n" + jwt

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}