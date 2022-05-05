@file:Suppress("NAME_SHADOWING")

package com.candra.submissiononeintermediate.helper.`object`

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.candra.submissiononeintermediate.view.EmailView
import com.candra.submissiononeintermediate.view.MyEditText
import com.candra.submissiononeintermediate.view.UsernameView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

object Animation {

    fun playAnimationImageSplash(image: ImageView){
        ObjectAnimator.ofFloat(image,View.TRANSLATION_Y,-30f,30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    fun playAniamtionUsername(thumbnail: ImageView,title: MaterialTextView,editUsername: UsernameView,
                              editEmail: EmailView, editPassword: MyEditText,daftarButton: MaterialButton,
                                textAtau: TextView,loginButton: MaterialButton)
    {
        ObjectAnimator.ofFloat(thumbnail,View.TRANSLATION_X,-30f,30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(title,View.ALPHA,1f).setDuration(500)
        val editUsername = ObjectAnimator.ofFloat(editUsername,View.ALPHA,1f).setDuration(500)
        val editEmail = ObjectAnimator.ofFloat(editEmail,View.ALPHA,1f).setDuration(500)
        val editPassword = ObjectAnimator.ofFloat(editPassword,View.ALPHA,1f).setDuration(500)
        val daftarButton = ObjectAnimator.ofFloat(daftarButton,View.ALPHA,1f).setDuration(500)
        val textAtau = ObjectAnimator.ofFloat(textAtau,View.ALPHA,1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(loginButton,View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,editUsername,editEmail,editPassword,daftarButton,textAtau,loginButton)
            start()
        }
    }

    fun playAnimationLogin(image: ImageView,title: MaterialTextView,usernamEditText: EmailView,passwordEditText: MyEditText,
    loginButton: MaterialButton,textAtau: TextView,daftarButton: MaterialButton)
    {
        ObjectAnimator.ofFloat(image,View.TRANSLATION_X,-30f,30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleText = ObjectAnimator.ofFloat(title,View.ALPHA,1f).setDuration(500)
        val usernameEditText = ObjectAnimator.ofFloat(usernamEditText,View.ALPHA,1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(passwordEditText,View.ALPHA,1f).setDuration(500)
        val logibButton = ObjectAnimator.ofFloat(loginButton,View.ALPHA,1f).setDuration(500)
        val textAtau = ObjectAnimator.ofFloat(textAtau,View.ALPHA,1f).setDuration(500)
        val daftar = ObjectAnimator.ofFloat(daftarButton,View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(titleText,usernameEditText,passwordEditText,logibButton,textAtau,daftar)
            start()
        }
    }

}