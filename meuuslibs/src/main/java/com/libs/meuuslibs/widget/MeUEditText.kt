package com.libs.meuuslibs.widget

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
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
import com.libs.meuuslibs.R
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
//        getAttrs(attrs, defStyle)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
//        getAttrs(attrs)
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

    private var backgroundBefore: Int = R.drawable.background_underline_black
    private var backgroundAfter: Int = R.drawable.background_underline_black
    private var backgroundDrawablesBefore: Array<Drawable>? = null
    private var backgroundDrawablesAfter: Array<Drawable>? = null

    private var hintTextColorBefore: Int = 0x000000
    private var hintTextColorAfter: Int = 0x000000

    private var hintTextSizeBefore: Int = 15
    private var hintTextSizeAfter: Int = 15

    private var hintTextVerticalPositionBefore: Int = center
    private var hintTextVerticalPositionAfter: Int = center

    private var hintTextHorizontalPositionBefore: Int = center
    private var hintTextHorizontalPositionAfter: Int = center

    private var animationDuration: Int = 1

    private fun setTypeArray(typedArray: TypedArray) {
        backgroundBefore = typedArray.getResourceId(R.styleable.MeUEditText_backgroundBefore, R.drawable.background_underline_black)
        backgroundAfter = typedArray.getResourceId(R.styleable.MeUEditText_backgroundAfter, backgroundBefore)
        backgroundDrawablesBefore = arrayOf(getDrawable(context, backgroundBefore)!!, getDrawable(context, backgroundAfter)!!)
        backgroundDrawablesAfter = arrayOf(getDrawable(context, backgroundAfter)!!, getDrawable(context, backgroundBefore)!!)
        v_root.background = TransitionDrawable(backgroundDrawablesBefore)

        //HintText
        val hintText = typedArray.getString(R.styleable.MeUEditText_hintText)
        tv_hintBefore.text = hintText
        tv_hintAfter.text = hintText
        tv_hint.text = hintText

        hintTextColorBefore = typedArray.getColor(R.styleable.MeUEditText_hintTextColorBefore, 0x000000)
        tv_hintBefore.setTextColor(ContextCompat.getColor(context, hintTextColorBefore))
        hintTextColorAfter = typedArray.getColor(R.styleable.MeUEditText_hintTextColorAfter, hintTextColorBefore)
        tv_hintAfter.setTextColor(ContextCompat.getColor(context, hintTextColorAfter))
        tv_hint.setTextColor(ContextCompat.getColor(context, hintTextColorBefore))

        hintTextSizeBefore = typedArray.getDimensionPixelSize(R.styleable.MeUEditText_hintTextSizeBefore, dpToPx(context, 15))
        tv_hintBefore.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeBefore.toFloat())
        hintTextSizeAfter = typedArray.getDimensionPixelSize(R.styleable.MeUEditText_hintTextSizeAfter, hintTextSizeBefore)
        tv_hintAfter.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeAfter.toFloat())
        tv_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeBefore.toFloat())

        hintTextVerticalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionBefore, 0)
        hintTextVerticalPositionAfter = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionBefore, hintTextVerticalPositionBefore)
        setVerticalPosition()

        hintTextHorizontalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionBefore, 0)
        hintTextHorizontalPositionAfter = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionAfter, hintTextHorizontalPositionBefore)
        setHorizontalPosition()

        animationDuration = typedArray.getInt(R.styleable.MeUEditText_animationDuration, 0)

        //EditText
        val editText = typedArray.getString(R.styleable.MeUEditText_editText)
        et_input.setText(editText)

        val editTextColor = typedArray.getColor(R.styleable.MeUEditText_editTextColor, 0x000000)
        et_input.setTextColor(editTextColor)

        val editTextSize = typedArray.getDimensionPixelSize(R.styleable.MeUEditText_editTextSize, dpToPx(context, 15))
        et_input.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize.toFloat())

        typedArray.recycle()
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
        val hintWidthDifference = (tv_hintBefore.layoutParams.width - tv_hintAfter.layoutParams.width).unaryPlus()
        val hintHeightDifference = (tv_hintBefore.layoutParams.height - tv_hintAfter.layoutParams.height).unaryPlus()

        val smallWidth = if (tv_hintBefore.layoutParams.width < tv_hintAfter.layoutParams.width) tv_hintBefore.layoutParams.width else tv_hintAfter.layoutParams.width
        val smallHeight = if (tv_hintBefore.layoutParams.height < tv_hintAfter.layoutParams.height) tv_hintBefore.layoutParams.height else tv_hintAfter.layoutParams.height

        val hintXDifference = tv_hintAfter.x - tv_hintBefore.x
        val hintYDifference = tv_hintAfter.y - tv_hintBefore.y

        if (et_input.hasFocus())
            mAnimator.setIntValues(hintTextColorBefore, hintTextColorAfter)
        else
            mAnimator.setIntValues(hintTextColorAfter, hintTextColorBefore)
        mAnimator.setEvaluator(ArgbEvaluator())
        mAnimator.duration = duration.toLong()
        mAnimator.interpolator = DecelerateInterpolator()
        mAnimator.addUpdateListener {
            tv_hint.setTextColor(ContextCompat.getColor(context, it.animatedValue as Int))
            val fraction = if (et_input.hasFocus()) it.animatedFraction else 1 - it.animatedFraction

            hintParams.width = (hintWidthDifference.toFloat() * fraction).toInt() + smallWidth
            hintParams.height = (hintHeightDifference.toFloat() * fraction).toInt() + smallHeight

            tv_hint.translationX = hintXDifference * fraction
            tv_hint.translationY = hintYDifference * fraction
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

//    private var disposable: Disposable? = null

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
//        setAnim(animationDuration)
    }

//    override fun onDetachedFromWindow() {
//        if (disposable != null && !disposable!!.isDisposed)
//            disposable?.dispose()
//        super.onDetachedFromWindow()
//    }

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
}