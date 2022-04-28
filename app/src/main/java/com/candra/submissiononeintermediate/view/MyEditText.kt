package com.candra.submissiononeintermediate.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.helper.Help

class MyEditText : AppCompatEditText,View.OnTouchListener
{
    private var txtColor: Int = 0

    private lateinit var enabledBackground: Drawable
    private lateinit var userDrawable: Drawable
    private lateinit var clearButtonImage: Drawable
    private var isHidden = true
    private lateinit var hidePasswordIcon : Drawable
    private lateinit var showPasswordIcon : Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context,attrs: AttributeSet) : super(context,attrs){
        init()
    }

    constructor(context: Context,attrs: AttributeSet,defStyleAttr: Int): super(context,attrs,defStyleAttr){
        init()
    }

    private fun init(){
        txtColor = ContextCompat.getColor(context,android.R.color.black)
        enabledBackground = ContextCompat.getDrawable(context,R.drawable.background_edit_text) as Drawable
        userDrawable = ContextCompat.getDrawable(context,R.drawable.ic_baseline_vpn_key_24) as Drawable
        clearButtonImage = ContextCompat.getDrawable(context,R.drawable.ic_baseline_close_24) as Drawable
        hidePasswordIcon = ContextCompat.getDrawable(context,R.drawable.eye_disabled) as Drawable
        showPasswordIcon = ContextCompat.getDrawable(context,R.drawable.ic_baseline_remove_red_eye_24) as Drawable

        setTextDrawbles(startOfTheText = userDrawable)
        setOnTouchListener(this)

        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                val textLowerCase = s.toString().lowercase().trim()
                when{
                    textLowerCase.length < 6  -> showError("Password ${resources.getString(R.string.errorValid)}")
                    textLowerCase.isEmpty() && textLowerCase.isBlank() -> showError(context.getString(
                                            R.string.password_empty))
                    else -> Log.d("Succes", "onTextChanged: Success")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        setTextColor(txtColor)
        setHintTextColor(txtColor)
        background = enabledBackground
        textSize = 18f
        hint = resources.getString(R.string.password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD


    }

    private fun setTextDrawbles(
        startOfTheText: Drawable? = null,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        transformationMethod = if (isHidden){
            showPassword()
            PasswordTransformationMethod.getInstance()
        }else{
            hiddenPassword()
            null
        }

    }

    private fun showError(message: String){
        setError(message,null)
    }


    private fun showPassword(){
        setTextDrawbles(null,null,null,null)
        setTextDrawbles(startOfTheText = userDrawable,endOfTheText = showPasswordIcon)
    }

    private fun hiddenPassword(){
        setTextDrawbles(null,null,null,null)
        setTextDrawbles(startOfTheText = userDrawable,endOfTheText = hidePasswordIcon)
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val toggleButtonStart: Float
            val toggleButtonEnd: Float
            var isToggleButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                toggleButtonEnd = (hidePasswordIcon.intrinsicWidth + paddingStart).toFloat()
                if (event.x < toggleButtonEnd) isToggleButtonClicked = true
            } else {
                toggleButtonStart = (width - paddingEnd - hidePasswordIcon.intrinsicWidth).toFloat()
                if (event.x > toggleButtonStart) isToggleButtonClicked = true
            }

            if (isToggleButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        hidePasswordIcon = ContextCompat.getDrawable(context, R.drawable.eye_disabled) as Drawable
                        showPasswordIcon =  ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_red_eye_24) as Drawable
                        isHidden = !isHidden
                        return true
                    }
                }
            }
        }
        return false
    }

}