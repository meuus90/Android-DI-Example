package com.libs.meuuslibs.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.libs.meuuslibs.R
import kotlinx.android.synthetic.main.widget_meu_edit_text.view.*


class MeUEditText : ViewGroup {
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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeUEditText)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeUEditText, defStyle, 0)
        setTypeArray(typedArray)
    }

    private var backgroundBeforeFocus: Int = R.drawable.background_underline_black
    private var backgroundAfterFocus: Int = R.drawable.background_underline_black
    private var hintTextColorBeforeFocus: Int = R.color.colorBlack
    private var hintTextColorAfterFocus: Int = R.color.colorBlack
    private var hintTextSizeBeforeFocus: Float = 15f
    private var hintTextSizeAfterFocus: Float = 15f

    private var hintTextHorizontalPositionBeforeFocus: String = "center"
    private var hintTextHorizontalPositionAfterFocus: String = "center"

    private var hintTextVerticalPositionBeforeFocus: String = "center"
    private var hintTextVerticalPositionAfterFocus: String = "center"

    private var hintTextOverlapBeforeFocus: Boolean = true
    private var hintTextOverlapAfterFocus: Boolean = true

    private fun setTypeArray(typedArray: TypedArray) {
        backgroundBeforeFocus = typedArray.getResourceId(R.styleable.MeUEditText_backgroundBeforeFocus, R.drawable.background_underline_black)
        backgroundAfterFocus = typedArray.getResourceId(R.styleable.MeUEditText_backgroundAfterFocus, backgroundBeforeFocus)
        v_root.setBackgroundResource(backgroundBeforeFocus)

        //HintText
        val hintText = typedArray.getString(R.styleable.MeUEditText_hintText)
        tv_hint.text = hintText

        hintTextColorBeforeFocus = typedArray.getResourceId(R.styleable.MeUEditText_hintTextColorBeforeFocus, R.color.colorBlack)
        hintTextColorAfterFocus = typedArray.getResourceId(R.styleable.MeUEditText_hintTextColorAfterFocus, hintTextColorBeforeFocus)
        tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorBeforeFocus))

        hintTextSizeBeforeFocus = typedArray.getFloat(R.styleable.MeUEditText_hintTextSizeBeforeFocus, 15f)
        hintTextSizeAfterFocus = typedArray.getFloat(R.styleable.MeUEditText_hintTextSizeAfterFocus, hintTextSizeBeforeFocus)
        tv_hint.textSize = hintTextSizeBeforeFocus


        //EditText
        val editText = typedArray.getString(R.styleable.MeUEditText_editText)
        et_input.setText(editText)

        val editTextColor = typedArray.getResourceId(R.styleable.MeUEditText_editTextColor, R.color.colorBlack)
        et_input.setTextColor(ContextCompat.getColor(context, editTextColor))

        val editTextSize = typedArray.getFloat(R.styleable.MeUEditText_editTextSize, 15f)
        et_input.textSize = editTextSize

        if (this.hasFocus()) {
            v_root.setBackgroundResource(backgroundBeforeFocus)
            tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorBeforeFocus))
        } else {
            v_root.setBackgroundResource(backgroundAfterFocus)
            tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorAfterFocus))
        }


        typedArray.recycle()
    }

    fun getHintText(text: String)  :TextView{
        return tv_hint
    }

    fun getEditText(text: String) :EditText{
        return et_input
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}