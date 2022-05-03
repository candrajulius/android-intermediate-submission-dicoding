package com.candra.submissiononeintermediate.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.activity.MapsActivity.Companion.TAG
import com.candra.submissiononeintermediate.databinding.SplashScreenActivityBinding
import com.candra.submissiononeintermediate.helper.`object`.Animation
import com.candra.submissiononeintermediate.helper.`object`.Contant
import com.candra.submissiononeintermediate.helper.`object`.SettingTheme
import com.candra.submissiononeintermediate.helper.yearFormat
import com.candra.submissiononeintermediate.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreen: AppCompatActivity(){

    private lateinit var binding: SplashScreenActivityBinding
    private val userViewModel by viewModels<UserViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Animation.playAnimationImageSplash(binding.gambarDeveloper)

        binding.textView.text = "Copy Right ${yearFormat()} ${getString(R.string.by_developer)}"

        Handler(mainLooper).postDelayed({
            userViewModel.getDataUser(this@SplashScreen).observe(this){ logiUser ->
               val targetActivity =  if (logiUser?.token?.isNotEmpty() == true) ListStroy::class.java else LoginActivity::class.java
                startActivity(Intent(this@SplashScreen,targetActivity))
                finish()
            }
        },5000L)
    }
}