package com.libs.meuuslibs.widget

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.libs.meuuslibs.util.ConvertMetrics.Companion.dpToPx
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
        val view = li.inflate(com.libs.meuuslibs.R.layout.widget_meu_edit_text, this, false)
        addView(view)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, com.libs.meuuslibs.R.styleable.MeUEditText)
        setTypeArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, com.libs.meuuslibs.R.styleable.MeUEditText, defStyle, 0)
        setTypeArray(typedArray)
    }

    private var backgroundBefore: Int = com.libs.meuuslibs.R.drawable.background_underline_black
    private var backgroundAfter: Int = com.libs.meuuslibs.R.drawable.background_underline_black
    private var backgroundDrawablesBefore: Array<Drawable>? = null
    private var backgroundDrawablesAfter: Array<Drawable>? = null

    private var hintTextColorBefore: Int = com.libs.meuuslibs.R.color.colorBlack
    private var hintTextColorAfter: Int = com.libs.meuuslibs.R.color.colorBlack
    private var hintTextDrawablesBefore: Array<Drawable>? = null
    private var hintTextDrawablesAfter: Array<Drawable>? = null

    private var hintTextSizeBefore: Int = 15
    private var hintTextSizeAfter: Int = 15

    private var hintTextVerticalPositionBefore: Int = center
    private var hintTextVerticalPositionAfter: Int = center

    private var hintTextHorizontalPositionBefore: Int = center
    private var hintTextHorizontalPositionAfter: Int = center

    private fun setTypeArray(typedArray: TypedArray) {
        backgroundBefore = typedArray.getResourceId(com.libs.meuuslibs.R.styleable.MeUEditText_backgroundBefore, com.libs.meuuslibs.R.drawable.background_underline_black)
        backgroundAfter = typedArray.getResourceId(com.libs.meuuslibs.R.styleable.MeUEditText_backgroundAfter, backgroundBefore)
        backgroundDrawablesBefore = arrayOf(getDrawable(context, backgroundBefore)!!, getDrawable(context, backgroundAfter)!!)
        backgroundDrawablesAfter = arrayOf(getDrawable(context, backgroundAfter)!!, getDrawable(context, backgroundBefore)!!)
        v_root.background = TransitionDrawable(backgroundDrawablesBefore)

        //HintText
        val hintText = typedArray.getString(com.libs.meuuslibs.R.styleable.MeUEditText_hintText)
        tv_hintBefore.text = hintText
        tv_hintAfter.text = hintText

        hintTextColorBefore = typedArray.getResourceId(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextColorBefore, com.libs.meuuslibs.R.color.colorBlack)
        tv_hintBefore.setTextColor(ContextCompat.getColor(context, hintTextColorBefore))
        hintTextColorAfter = typedArray.getResourceId(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextColorAfter, hintTextColorBefore)
        tv_hintAfter.setTextColor(ContextCompat.getColor(context, hintTextColorAfter))
        tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorBefore))

        hintTextSizeBefore = typedArray.getDimensionPixelSize(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextSizeBefore, dpToPx(context, 15))
        tv_hintBefore.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeBefore.toFloat())
        hintTextSizeAfter = typedArray.getDimensionPixelSize(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextSizeAfter, hintTextSizeBefore)
        tv_hintAfter.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeAfter.toFloat())
        tv_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeBefore.toFloat())

        hintTextVerticalPositionBefore = typedArray.getInt(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextVerticalPositionBefore, 0)
        hintTextVerticalPositionAfter = typedArray.getInt(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextVerticalPositionBefore, hintTextVerticalPositionBefore)
        setVerticalPosition()

        hintTextHorizontalPositionBefore = typedArray.getInt(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextHorizontalPositionBefore, 0)
        hintTextHorizontalPositionAfter = typedArray.getInt(com.libs.meuuslibs.R.styleable.MeUEditText_hintTextHorizontalPositionAfter, hintTextHorizontalPositionBefore)
        setHorizontalPosition()


        //EditText
        val editText = typedArray.getString(com.libs.meuuslibs.R.styleable.MeUEditText_editText)
        et_input.setText(editText)

        val editTextColor = typedArray.getResourceId(com.libs.meuuslibs.R.styleable.MeUEditText_editTextColor, com.libs.meuuslibs.R.color.colorBlack)
        et_input.setTextColor(ContextCompat.getColor(context, editTextColor))

        val editTextSize = typedArray.getDimensionPixelSize(com.libs.meuuslibs.R.styleable.MeUEditText_editTextSize, dpToPx(context, 15))
        et_input.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize.toFloat())


//        if (this.has()) {
//            v_root.setBackgroundResource(backgroundBefore)
//            tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorBefore))
//        } else {
//            v_root.setBackgroundResource(backgroundAfter)
//            tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorAfter))
//        }


        typedArray.recycle()
    }

    fun getHint(): String {
        return tv_hintBefore.text.toString()
    }

    fun setHint(hint: String) {
        tv_hint.text = hint
        tv_hintBefore.text = hint
        tv_hintAfter.text = hint
    }

    fun getEditText(): EditText {
        return et_input
    }

    private fun setVerticalPosition() {
        val paramsHintBefore = tv_hintBefore.layoutParams as RelativeLayout.LayoutParams
        val paramsHintAfter = tv_hintAfter.layoutParams as RelativeLayout.LayoutParams
        val paramsInput = et_input.layoutParams as RelativeLayout.LayoutParams

        when {
            hintTextVerticalPositionBefore == top && hintTextVerticalPositionAfter == top -> {
                when {
                    hintTextSizeBefore < hintTextSizeAfter -> paramsInput.topMargin = hintTextSizeAfter
                    else -> paramsInput.topMargin = hintTextSizeBefore
                }
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            }

            hintTextVerticalPositionBefore == top && hintTextVerticalPositionAfter == center -> {
                paramsInput.topMargin = hintTextSizeBefore
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL)
            }

            hintTextVerticalPositionBefore == top && hintTextVerticalPositionAfter == bottom -> {
                paramsInput.topMargin = hintTextSizeBefore
                paramsInput.bottomMargin = hintTextSizeAfter
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }

            hintTextVerticalPositionBefore == center && hintTextVerticalPositionAfter == top -> {
                paramsInput.topMargin = hintTextSizeAfter
                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            }

            hintTextVerticalPositionBefore == center && hintTextVerticalPositionAfter == center -> {
                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL)
                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL)
            }

            hintTextVerticalPositionBefore == center && hintTextVerticalPositionAfter == bottom -> {
                paramsInput.bottomMargin = hintTextSizeAfter
                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }

            hintTextVerticalPositionBefore == bottom && hintTextVerticalPositionAfter == top -> {
                paramsInput.topMargin = hintTextSizeAfter
                paramsInput.bottomMargin = hintTextSizeBefore
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            }

            hintTextVerticalPositionBefore == bottom && hintTextVerticalPositionAfter == center -> {
                paramsInput.bottomMargin = hintTextSizeBefore
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL)
            }

            hintTextVerticalPositionBefore == bottom && hintTextVerticalPositionAfter == bottom -> {
                when {
                    hintTextSizeBefore < hintTextSizeAfter -> paramsInput.topMargin = hintTextSizeAfter
                    else -> paramsInput.topMargin = hintTextSizeBefore
                }
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
        }
        et_input.layoutParams = paramsInput
        tv_hintBefore.layoutParams = paramsHintBefore
        tv_hintAfter.layoutParams = paramsHintAfter
        tv_hint.layoutParams = paramsHintBefore
    }

    private fun setHorizontalPosition() {
        val paramsHintBefore = tv_hintBefore.layoutParams as RelativeLayout.LayoutParams
        when (hintTextHorizontalPositionBefore) {
            left, start -> paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_START)
            right, end -> paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_END)
            else -> paramsHintBefore.addRule(RelativeLayout.CENTER_HORIZONTAL)
        }
        tv_hintBefore.layoutParams = paramsHintBefore

        val paramsHintAfter = tv_hintAfter.layoutParams as RelativeLayout.LayoutParams
        when (hintTextHorizontalPositionBefore) {
            left, start -> paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_START)
            right, end -> paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_END)
            else -> paramsHintAfter.addRule(RelativeLayout.CENTER_HORIZONTAL)
        }
        tv_hintAfter.layoutParams = paramsHintAfter

        tv_hint.layoutParams = paramsHintBefore
    }

    private fun setAnim(duration: Int) {
        val mAnimator = ValueAnimator()
        val hintParams = tv_hint.layoutParams
        val hintWidthDifference = (tv_hintBefore.layoutParams.height - tv_hintAfter.layoutParams.height).unaryPlus()
        val hintHeightDifference = (tv_hintBefore.layoutParams.width - tv_hintAfter.layoutParams.width).unaryPlus()
        if (et_input.hasFocus())
            mAnimator.setIntValues(hintTextColorBefore, hintTextColorAfter)
        else
            mAnimator.setIntValues(hintTextColorAfter, hintTextColorBefore)
        mAnimator.setEvaluator(ArgbEvaluator())
        mAnimator.duration = duration.toLong()
        mAnimator.interpolator = DecelerateInterpolator()
        mAnimator.addUpdateListener {
            tv_hint.setTextColor(ContextCompat.getColor(context, it.animatedValue as Int))
            val fraction = it.animatedFraction

            if(et_input.hasFocus()) {
                hintParams.width = (hintWidthDifference.toFloat() * fraction).toInt()  //todo:refactoring
                hintParams.height = (hintHeightDifference.toFloat() * fraction).toInt()  //todo:refactoring

//                tv_hint.getLocationInWindow()
            }




            //size
            //position x
            //position y


        }
        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                if (et_input.hasFocus()) {
                    v_root.background = TransitionDrawable(backgroundDrawablesBefore)
                    (v_root.background as TransitionDrawable).startTransition(duration)
                } else {
                    v_root.background = TransitionDrawable(backgroundDrawablesAfter)
                    (v_root.background as TransitionDrawable).startTransition(duration)
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }
        })
        mAnimator.start()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        setAnim(300)
    }
}