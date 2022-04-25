package com.candra.submissiononeintermediate.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.databinding.SplashScreenActivityBinding
import com.candra.submissiononeintermediate.helper.Animation

@SuppressLint("CustomSplashScreen")
class SplashScreen: AppCompatActivity(){

    private lateinit var binding: SplashScreenActivityBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Animation.playAnimationImageSplash(binding.gambarDeveloper)

        binding.textView.text = "@2022 By ${getString(R.string.name_developer)}"

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
            finish()
        },5000)
    }
}