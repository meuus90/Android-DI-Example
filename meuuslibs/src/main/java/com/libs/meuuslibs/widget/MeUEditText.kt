package com.libs.meuuslibs.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.libs.meuuslibs.R
import com.libs.meuuslibs.util.ConvertMetrics.Companion.dpToPx
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.widget_meu_edit_text.view.*
import java.util.concurrent.TimeUnit


class MeUEditText : RelativeLayout {
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

    private var backgroundBefore: Int = R.drawable.background_underline_black
    private var backgroundAfter: Int = R.drawable.background_underline_black
    private var backgroundDrawablesBefore: Array<Drawable>? = arrayOf(getDrawable(context, backgroundBefore)!!, getDrawable(context, backgroundAfter)!!)
    private var backgroundDrawablesAfter: Array<Drawable>? = arrayOf(getDrawable(context, backgroundBefore)!!, getDrawable(context, backgroundAfter)!!)

    private var hintTextColorBefore: Int = 0x000000
    private var hintTextColorAfter: Int = 0x000000

    private var hintTextSizeBefore: Int = 15
    private var hintTextSizeAfter: Int = 15

    private var hintTextVerticalPositionBefore: Int = center
    private var hintTextVerticalPositionAfter: Int = center

    private var hintTextHorizontalPositionBefore: Int = center
    private var hintTextHorizontalPositionAfter: Int = center

    private var animationDuration: Long = 100

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

        hintTextColorBefore = typedArray.getColor(R.styleable.MeUEditText_hintTextColorBefore, ContextCompat.getColor(context, R.color.colorBlack))
        tv_hintBefore.setTextColor(hintTextColorBefore)
        hintTextColorAfter = typedArray.getColor(R.styleable.MeUEditText_hintTextColorAfter, hintTextColorBefore)
        tv_hintAfter.setTextColor(hintTextColorAfter)
        tv_hint.setTextColor(hintTextColorBefore)

        hintTextSizeBefore = typedArray.getDimensionPixelSize(R.styleable.MeUEditText_hintTextSizeBefore, dpToPx(context, 15))
        tv_hintBefore.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeBefore.toFloat())
        hintTextSizeAfter = typedArray.getDimensionPixelSize(R.styleable.MeUEditText_hintTextSizeAfter, hintTextSizeBefore)
        tv_hintAfter.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeAfter.toFloat())
        tv_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeBefore.toFloat())

        hintTextVerticalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionBefore, center)
        hintTextVerticalPositionAfter = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionAfter, hintTextVerticalPositionBefore)
        setVerticalPosition()

        hintTextHorizontalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionBefore, center)
        hintTextHorizontalPositionAfter = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionAfter, hintTextHorizontalPositionBefore)
        setHorizontalPosition()

        animationDuration = typedArray.getInt(R.styleable.MeUEditText_animationDuration, 100).toLong()

        //EditText
        val editText = typedArray.getString(R.styleable.MeUEditText_editText)
        et_input.setText(editText ?: "")

        val editTextColor = typedArray.getColor(R.styleable.MeUEditText_editTextColor, ContextCompat.getColor(context, R.color.colorBlack))
        et_input.setTextColor(editTextColor)

        val editTextSize = typedArray.getDimensionPixelSize(R.styleable.MeUEditText_editTextSize, dpToPx(context, 15))
        et_input.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize.toFloat())




        Log.e("background", "backgroundBefore : $backgroundBefore, backgroundAfter : $backgroundAfter")
        Log.e("hint", "hintText : $hintText")
        Log.e("hintColor", "hintTextColorBefore : $hintTextColorBefore, hintTextColorAfter : $hintTextColorAfter")
        Log.e("hintSize", "hintTextSizeBefore : $hintTextSizeBefore, hintTextSizeAfter : $hintTextSizeAfter")
        Log.e("hintVerticalPosition", "hintTextVerticalPositionBefore : $hintTextVerticalPositionBefore, hintTextVerticalPositionAfter : $hintTextVerticalPositionAfter")
        Log.e("hintHorizontalPosition", "hintTextHorizontalPositionBefore : $hintTextHorizontalPositionBefore, hintTextHorizontalPositionAfter : $hintTextHorizontalPositionAfter")
        Log.e("editText", "editText : $editText, editTextColor : $editTextColor, editTextSize : $editTextSize")
        typedArray.recycle()
    }

    private fun setVerticalPosition() {
        val paramsHintBefore = tv_hintBefore.layoutParams as RelativeLayout.LayoutParams
        val paramsHintAfter = tv_hintAfter.layoutParams as RelativeLayout.LayoutParams
        val paramsInput = et_input.layoutParams as RelativeLayout.LayoutParams

        when {
            hintTextVerticalPositionBefore == top && hintTextVerticalPositionAfter == top -> {
                when {
                    hintTextSizeBefore > hintTextSizeAfter -> paramsInput.addRule(RelativeLayout.BELOW, tv_hintBefore.id)
                    else -> paramsInput.addRule(RelativeLayout.BELOW, tv_hintAfter.id)
                }
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == top && hintTextVerticalPositionAfter == center -> {
                paramsInput.addRule(RelativeLayout.BELOW, tv_hintBefore.id)
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == top && hintTextVerticalPositionAfter == bottom -> {
                paramsInput.addRule(RelativeLayout.BELOW, tv_hintBefore.id)
                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintAfter.id)
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == center && hintTextVerticalPositionAfter == top -> {
                paramsInput.addRule(RelativeLayout.BELOW, tv_hintAfter.id)
                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == center && hintTextVerticalPositionAfter == center -> {
                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == center && hintTextVerticalPositionAfter == bottom -> {
                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintAfter.id)
                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == bottom && hintTextVerticalPositionAfter == top -> {
                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintBefore.id)
                paramsInput.addRule(RelativeLayout.BELOW, tv_hintAfter.id)
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == bottom && hintTextVerticalPositionAfter == center -> {
                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintBefore.id)
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            }

            hintTextVerticalPositionBefore == bottom && hintTextVerticalPositionAfter == bottom -> {
                when {
                    hintTextSizeBefore > hintTextSizeAfter -> paramsInput.addRule(RelativeLayout.ABOVE, tv_hintBefore.id)
                    else -> paramsInput.addRule(RelativeLayout.ABOVE, tv_hintAfter.id)
                }
                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
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

    private fun setAnim(duration: Long, isFocused: Boolean) {
        val hintSizeDifference = tv_hintBefore.textSize - tv_hintAfter.textSize

        val smallSize = if (hintSizeDifference < 0) tv_hintBefore.textSize else tv_hintAfter.textSize

        val hintXDifference = tv_hintAfter.x - tv_hintBefore.x
        val hintYDifference = tv_hintAfter.y - tv_hintBefore.y

        val colorAnimator = ValueAnimator()
        if (isFocused)
            colorAnimator.setIntValues(hintTextColorBefore, hintTextColorAfter)
        else
            colorAnimator.setIntValues(hintTextColorAfter, hintTextColorBefore)
        colorAnimator.setEvaluator(ArgbEvaluator())
        colorAnimator.duration = duration
        colorAnimator.interpolator = DecelerateInterpolator()
        colorAnimator.addUpdateListener {
            tv_hint.setTextColor(it.animatedValue as Int)
        }

        val scaleAnimator = if (isFocused) ValueAnimator.ofFloat(0f, 1f) else ValueAnimator.ofFloat(1f, 0f)
        scaleAnimator.duration = duration
        scaleAnimator.interpolator = DecelerateInterpolator()
        scaleAnimator.addUpdateListener {
            val floatValue = it.animatedValue as Float

            tv_hint.textSize = hintSizeDifference * floatValue + smallSize

            tv_hint.translationX = hintXDifference * floatValue
            tv_hint.translationY = hintYDifference * floatValue
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(colorAnimator, scaleAnimator)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                if (isFocused) {
                    v_root.background = TransitionDrawable(backgroundDrawablesBefore)
                    (v_root.background as TransitionDrawable).startTransition(duration.toInt())
                } else {
                    v_root.background = TransitionDrawable(backgroundDrawablesAfter)
                    (v_root.background as TransitionDrawable).startTransition(duration.toInt())
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }
        })


        animatorSet.start()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        disposable = RxView.focusChanges(et_input)
                .throttleFirst(animationDuration, TimeUnit.MILLISECONDS)
                .subscribe {
                    setAnim(animationDuration, it)
                }
    }

    var disposable: Disposable? = null
    override fun onDetachedFromWindow() {
        if (disposable != null && !disposable!!.isDisposed)
            disposable?.dispose()
        super.onDetachedFromWindow()
    }

    fun getHint(): String {
        return tv_hint.text.toString()
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