package com.libs.meuuslibs.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.libs.meuuslibs.R
import kotlinx.android.synthetic.main.widget_meu_edit_text.view.*


class MEUEditText : RelativeLayout {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView(context)
        getAttrs(attrs, defStyle)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
        getAttrs(attrs)
    }

    constructor(context: Context) : super(context, null) {
        initView(context)
    }

    private fun initView(context: Context) {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.widget_meu_edit_text, this, false)
        addView(view)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MEUEditText)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MEUEditText, defStyle, 0)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val backgroundResId = typedArray.getResourceId(R.styleable.MEUEditText_background, R.drawable.background_underline_black)
        v_root.setBackgroundResource(backgroundResId)

        //HintText
        val hintText = typedArray.getString(R.styleable.MEUEditText_hintText)
        tv_hint.text = hintText

        val hintTextColor = typedArray.getResourceId(R.styleable.MEUEditText_hintTextColor, R.color.colorBlack)
        tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColor))

        val hintTextSize = typedArray.getFloat(R.styleable.MEUEditText_hintTextSize, 15f)
        tv_hint.textSize = hintTextSize

        //EditText
        val editText = typedArray.getString(R.styleable.MEUEditText_editText)
        et_input.setText(editText)

        val editTextColor = typedArray.getResourceId(R.styleable.MEUEditText_editTextColor, R.color.colorBlack)
        et_input.setTextColor(ContextCompat.getColor(context, editTextColor))

        val editTextSize = typedArray.getFloat(R.styleable.MEUEditText_editTextSize, 15f)
        et_input.textSize = editTextSize

        typedArray.recycle()
    }

    fun setHintText(text: String) {
        tv_hint.text = text
    }

    fun setHintTextColor(color: Int) {
        tv_hint.setTextColor(color)
    }

    fun setHintTextSize(size: Float) {
        tv_hint.textSize = size
    }


    fun setEditText(text: String) {
        et_input.setText(text)
    }

    fun setEditTextColor(color: Int) {
        et_input.setTextColor(color)
    }

    fun setEditTextSize(size: Float) {
        et_input.textSize = size
    }
}