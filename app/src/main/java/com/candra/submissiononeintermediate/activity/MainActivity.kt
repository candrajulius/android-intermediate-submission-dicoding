package com.candra.submissiononeintermediate.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.databinding.ActivityMainBinding
import com.candra.submissiononeintermediate.helper.`object`.Animation
import com.candra.submissiononeintermediate.helper.`object`.Help
import com.candra.submissiononeintermediate.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            Animation.playAniamtionUsername(gambarAccount,title,editTextUsername,editTextEmail,editTextPassword,
                signUpButton,textOr,login)

            editTextEmail.addTextChangedListener(registerTextWatcher)
            editTextUsername.addTextChangedListener(registerTextWatcher)
            editTextPassword.addTextChangedListener(registerTextWatcher)
        }

        Help.showToolbar(this@MainActivity,supportActionBar,resources.getString(R.string.sign_up),2)

        validateConfirm()


        readAllDataResponse()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun readAllDataResponse(){
        userViewModel.loading.observe(this){
            binding.apply {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                signUpButton.visibility =  if(it) View.GONE else View.VISIBLE
            }
        }

        userViewModel.messageSuccess.observe(this){
            Help.showToast(this@MainActivity,it)
        }

        userViewModel.messageError.observe(this){ message ->
            Help.showDialog(this@MainActivity,message)
        }

        userViewModel.success.observe(this){
            if (it) showSuccess() else showFailed()
        }
    }

    private fun showSuccess(){
        binding.apply {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
            progressBar.visibility = View.GONE
            signUpButton.visibility = View.VISIBLE
            Help.setClearText(username = editTextUsername, password = editTextPassword, email = editTextEmail)
        }
    }

    private fun showFailed(){
        binding.apply {
            progressBar.visibility = View.GONE
            signUpButton.visibility = View.VISIBLE
            Help.setClearText(username = editTextUsername, password = editTextPassword, email = editTextEmail)
        }
    }



    private fun validateConfirm(){
        binding.apply {
            signUpButton.setOnClickListener {
                val username = editTextUsername.text.toString().lowercase().trim()
                val email = editTextEmail.text.toString().lowercase().trim()
                val password = editTextPassword.text.toString().lowercase().trim()

                lifecycleScope.launch {
                    userViewModel.registerAccountData(username,email,password)
                }
            }
            login.setOnClickListener {
                startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                finish()
            }
        }
    }

    private val registerTextWatcher = object: TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Your Code In Here
        }

        override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
            setButtoEnable()
        }

        override fun afterTextChanged(p0: Editable?) {
            // Your Code In Here
        }
    }

    private fun setButtoEnable(){

        binding.apply {
            val username = editTextUsername.text.toString().lowercase().trim()
            val password = editTextPassword.text.toString().lowercase().trim()
            val email = editTextEmail.text.toString().lowercase().trim()
            signUpButton.isEnabled = username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()
        }

    }
}