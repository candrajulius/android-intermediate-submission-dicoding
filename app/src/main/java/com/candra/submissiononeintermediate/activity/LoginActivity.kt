package com.candra.submissiononeintermediate.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.databinding.LoginActivityBinding
import com.candra.submissiononeintermediate.helper.Animation
import com.candra.submissiononeintermediate.helper.Help
import com.candra.submissiononeintermediate.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity: AppCompatActivity()
{
    private lateinit var binding: LoginActivityBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            Animation.playAnimationLogin(gambarAccount,title,editTextEmail,editTextPassword,buttonLogin,textOr,btnDaftar)

            editTextEmail.addTextChangedListener(registerWatcher)
            editTextPassword.addTextChangedListener(registerWatcher)
        }

        setUpClickListener()

        Help.showToolbar(this@LoginActivity,supportActionBar,resources.getString(R.string.login),1)

        readResponseViewModel()

    }

    private fun readResponseViewModel(){
        userViewModel.loading.observe(this){
            binding.apply {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                buttonLogin.visibility = if (it) View.GONE else View.VISIBLE
                textOr.visibility = if (it) View.GONE else View.VISIBLE
                btnDaftar.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        userViewModel.messageError.observe(this){
            Help.showDialog(this@LoginActivity,it)
        }

        userViewModel.messageSuccess.observe(this){
            Help.showToast(this@LoginActivity,it)
        }

        userViewModel.success.observe(this){ success ->
            if (success) setSuccessFully() else setFailedData()
        }

        userViewModel.getDataUser(this@LoginActivity).observe(this){ loginUser ->
            if (loginUser.isLogginIn == true){
                startActivity(Intent(this@LoginActivity,ListStroy::class.java))
                finish()
            }
        }
    }


    private fun setUpClickListener(){
        binding.apply {
            buttonLogin.setOnClickListener {
                val emailEdit = editTextEmail.text.toString().lowercase().trim()
                val passwordEditText = editTextPassword.text.toString().lowercase().trim()

                lifecycleScope.launch {
                    userViewModel.loginAccount(this@LoginActivity,emailEdit,passwordEditText)
                }
            }

            btnDaftar.setOnClickListener {
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish()
            }

        }
    }

    private val registerWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Your Code In Here
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
          setButtonEnabled()
        }

        override fun afterTextChanged(p0: Editable?) {
            // Your Code In Here
        }

    }

    private fun setSuccessFully(){
        startActivity(Intent(this@LoginActivity,ListStroy::class.java))
        finish()
        binding.apply {
            buttonLogin.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            Help.setClearText(email = editTextEmail, password = editTextPassword, username = null)
        }
    }

    private fun setFailedData(){
        binding.apply {
            buttonLogin.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            Help.setClearText(email = editTextEmail, password = editTextPassword, username = null)
        }
    }

    private fun setButtonEnabled(){
        binding.apply {
            val emailEditText = editTextEmail.text.toString().lowercase().trim()
            val passwordEditText = editTextPassword.text.toString().lowercase().trim()

            buttonLogin.isEnabled = emailEditText.isNotEmpty() && passwordEditText.isNotEmpty()
        }
    }

}