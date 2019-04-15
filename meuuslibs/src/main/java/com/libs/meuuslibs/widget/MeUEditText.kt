package com.libs.meuuslibs.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.libs.meuuslibs.R
import kotlinx.android.synthetic.main.widget_meu_edit_text.view.*


class MeUEditText : ViewGroup {
    companion object {
        const val top = -1
        const val bottom = 1
        const val left = -1
        const val start = -1
        const val right = 1
        const val end = 1
        const val center = 0

    }

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

    private var hintTextVerticalPositionBeforeFocus: Int = center
    private var hintTextVerticalPositionAfterFocus: Int = center

    private var hintTextHorizontalPositionBeforeFocus: Int = center
    private var hintTextHorizontalPositionAfterFocus: Int = center

    private fun setTypeArray(typedArray: TypedArray) {
        backgroundBeforeFocus = typedArray.getResourceId(R.styleable.MeUEditText_backgroundBeforeFocus, R.drawable.background_underline_black)
        backgroundAfterFocus = typedArray.getResourceId(R.styleable.MeUEditText_backgroundAfterFocus, backgroundBeforeFocus)
        v_root.setBackgroundResource(backgroundBeforeFocus)

        //HintText
        val hintText = typedArray.getString(R.styleable.MeUEditText_hintText)
        tv_hintBeforeFocus.text = hintText
        tv_hintAfterFocus.text = hintText

        hintTextColorBeforeFocus = typedArray.getResourceId(R.styleable.MeUEditText_hintTextColorBeforeFocus, R.color.colorBlack)
        tv_hintBeforeFocus.setTextColor(ContextCompat.getColor(context, hintTextColorBeforeFocus))
        hintTextColorAfterFocus = typedArray.getResourceId(R.styleable.MeUEditText_hintTextColorAfterFocus, hintTextColorBeforeFocus)
        tv_hintAfterFocus.setTextColor(ContextCompat.getColor(context, hintTextColorAfterFocus))

        hintTextSizeBeforeFocus = typedArray.getFloat(R.styleable.MeUEditText_hintTextSizeBeforeFocus, 15f)
        tv_hintBeforeFocus.textSize = hintTextSizeBeforeFocus
        hintTextSizeAfterFocus = typedArray.getFloat(R.styleable.MeUEditText_hintTextSizeAfterFocus, hintTextSizeBeforeFocus)
        tv_hintAfterFocus.textSize = hintTextSizeAfterFocus

        hintTextVerticalPositionBeforeFocus = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionBeforeFocus, 0)
        setVerticalPosition(tv_hintBeforeFocus, hintTextVerticalPositionBeforeFocus)
        hintTextVerticalPositionAfterFocus = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionBeforeFocus, hintTextVerticalPositionBeforeFocus)
        setVerticalPosition(tv_hintAfterFocus, hintTextVerticalPositionAfterFocus)

        hintTextHorizontalPositionBeforeFocus = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionBeforeFocus, 0)
        setHorizontalPosition(tv_hintBeforeFocus, hintTextHorizontalPositionBeforeFocus)
        hintTextHorizontalPositionAfterFocus = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionAfterFocus, hintTextHorizontalPositionBeforeFocus)
        setHorizontalPosition(tv_hintAfterFocus, hintTextHorizontalPositionAfterFocus)


        //EditText
        val editText = typedArray.getString(R.styleable.MeUEditText_editText)
        et_input.setText(editText)

        val editTextColor = typedArray.getResourceId(R.styleable.MeUEditText_editTextColor, R.color.colorBlack)
        et_input.setTextColor(ContextCompat.getColor(context, editTextColor))

        val editTextSize = typedArray.getFloat(R.styleable.MeUEditText_editTextSize, 15f)
        et_input.textSize = editTextSize


//        if (this.hasFocus()) {
//            v_root.setBackgroundResource(backgroundBeforeFocus)
//            tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorBeforeFocus))
//        } else {
//            v_root.setBackgroundResource(backgroundAfterFocus)
//            tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorAfterFocus))
//        }


        typedArray.recycle()
    }

    fun getHint(): String {
        return tv_hintBeforeFocus.text.toString()
    }

    fun setHint(hint: String) {
        tv_hintBeforeFocus.text = hint
        tv_hintAfterFocus.text = hint
    }

    fun getEditText(): EditText {
        return et_input
    }

    private fun setVerticalPosition(textView: TextView, position: Int) {
        val params = textView.layoutParams as RelativeLayout.LayoutParams
        when (position) {
            top -> params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            bottom -> params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            else -> params.addRule(RelativeLayout.CENTER_VERTICAL)
        }
        textView.layoutParams = params
    }

    private fun setHorizontalPosition(textView: TextView, position: Int) {
        val params = textView.layoutParams as RelativeLayout.LayoutParams
        when (position) {
            left, start -> params.addRule(RelativeLayout.ALIGN_PARENT_START)
            right, end -> params.addRule(RelativeLayout.ALIGN_PARENT_END)
            else -> params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        }
        textView.layoutParams = params
    }

    fun setAnim() {
        val scaleAnimator = if (et_input.isFocused) ValueAnimator.ofFloat(0f, 1f) else ValueAnimator.ofFloat(1f, 0f)
        scaleAnimator.duration = 300
        scaleAnimator.interpolator = DecelerateInterpolator()
        scaleAnimator.addUpdateListener {


        }
        scaleAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }
        })
        scaleAnimator.start()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}