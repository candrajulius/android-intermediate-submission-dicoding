package com.candra.submissiononeintermediate.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.candra.submissiononeintermediate.R

class UsernameView : AppCompatEditText,View.OnTouchListener
{
    private lateinit var enabledBackground: Drawable
    private var txtColor: Int = 0
    private lateinit var userDrawable: Drawable
    private lateinit var clearButtonImage: Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context,attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context,attrs,defStyleAttr){
        init()
    }


    private fun init(){
        enabledBackground = ContextCompat.getDrawable(context,R.drawable.background_edit_text)!!
        txtColor = ContextCompat.getColor(context,android.R.color.black)
        userDrawable = ContextCompat.getDrawable(context,R.drawable.ic_baseline_account_box_24)!!
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable

        setTextDrawbles(startOfTheText = userDrawable)
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                val textLowerCase = s.toString().lowercase().trim()
                when{
                    textLowerCase.isEmpty() -> showError("Username ${resources.getString(R.string.kosong)}",1)
                    textLowerCase.length < 6 -> showError("Username ${resources.getString(R.string.errorValid)}",2)
                    else -> showError(null,3)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        setTextColor(txtColor)
        setHintTextColor(txtColor)
        background = enabledBackground
        textSize = 18f
        hint = resources.getString(R.string.username)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
    }

    private fun showError(message: String?, position: Int){
        when(position){
            1 -> {
                setError(message,null)
                hideClearButton()
            }
            2 -> {
                showClearButton()
                setError(message,null)
            }
            3 -> {
                showClearButton()
            }
        }
    }

    private fun setTextDrawbles(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )

    }


    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else{
                return false
            }
        }
        return false
    }

    private fun showClearButton() {
        setTextDrawbles(startOfTheText = userDrawable,endOfTheText = clearButtonImage)
    }
    private fun hideClearButton() {
        setTextDrawbles(startOfTheText = userDrawable)
    }
}