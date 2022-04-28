package com.candra.submissiononeintermediate.helper

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.view.EmailView
import com.candra.submissiononeintermediate.view.MyEditText
import com.candra.submissiononeintermediate.view.UsernameView

object Help {
    fun isValidEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun showToolbar(context: Context, actionBar: ActionBar?, subTitleName: String, position: Int){
        if (position == 1){
            actionBar?.apply {
                title = context.resources.getString(R.string.name_developer)
                subtitle = subTitleName
            }
        }else if (position == 2){
            actionBar?.apply {
                title = context.resources.getString(R.string.name_developer)
                subtitle = subTitleName
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }


    fun showToast(context: Context, message: String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun showDialog(context: Context, message: String){
        AlertDialog.Builder(context)
            .setMessage(message)
            .setTitle("Peringatan")
            .setIcon(R.drawable.ic_launcher_foreground)
            .setPositiveButton("Ok"){dialog,_ ->
                dialog.dismiss()
            }.show()
    }


    fun setClearText(username: UsernameView?, password: MyEditText, email: EmailView){
        username?.text?.clear()
        password.text?.clear()
        email.text?.clear()
    }

    fun showDialogPermissionDenied(context: Context,message: String){
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setMessage("Aplikasi ini membutuhkan fitur perizinan $message anda.. " +
                    "Jika fitur ini tidak anda aktifkan. Anda tidak bisa menggunakan fitur ini.." +
                    "Silahkan pergi ke Setting anda")
            .setTitle("Peringatan")
            .setIcon(R.mipmap.ic_launcher_round)
            .setPositiveButton("Pergi ke Setting"){_,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",context.packageName,null)
                    intent.data = uri
                    context.startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                    Log.d(Contant.TAG, "showDialogPermissionDenied: ${e.message.toString()}")
                }
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }.show()
    }

}
