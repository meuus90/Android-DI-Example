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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.view.RxView
import com.libs.meuuslibs.R
import com.libs.meuuslibs.util.ConvertMetrics.Companion.dpToPx
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.widget_meu_edit_text.view.*
import java.util.concurrent.TimeUnit


class MeUEditText : ConstraintLayout {
    companion object {
        const val positionTop = -1
        const val positionBottom = 1
        const val positionStart = -1
        const val positionEnd = 1
        const val positionCenter = 0

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

    private var hintTextVerticalPositionBefore: Int = positionCenter
    private var hintTextVerticalPositionAfter: Int = positionCenter

    private var hintTextHorizontalPositionBefore: Int = positionCenter
    private var hintTextHorizontalPositionAfter: Int = positionCenter

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

        hintTextVerticalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionBefore, positionCenter)
        hintTextVerticalPositionAfter = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionAfter, hintTextVerticalPositionBefore)
        setVerticalPosition()

        hintTextHorizontalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionBefore, positionCenter)
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
        val paramsHintBefore = tv_hintBefore.layoutParams as ConstraintLayout.LayoutParams
        val paramsHintAfter = tv_hintAfter.layoutParams as ConstraintLayout.LayoutParams
        val paramsInput = et_input.layoutParams as ConstraintLayout.LayoutParams


        val constraintSet = ConstraintSet()
        constraintSet.addToVerticalChain(et_input.id, 0, 0)

        when(hintTextVerticalPositionBefore){
            positionTop -> constraintSet.addToVerticalChain(et_input.id, tv_hintBefore.id, 0)
            positionBottom -> constraintSet.addToVerticalChain(et_input.id, 0, tv_hintBefore.id)
            else -> constraintSet.addToVerticalChain(et_input.id, 0, 0)
        }



//        when {
//            hintTextVerticalPositionBefore == positionTop && hintTextVerticalPositionAfter == positionTop -> {
//                when {
//                    hintTextSizeBefore > hintTextSizeAfter -> paramsInput.addRule(RelativeLayout.BELOW, tv_hintBefore.id)
//                    else -> paramsInput.addRule(RelativeLayout.BELOW, tv_hintAfter.id)
//                }
//                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionTop && hintTextVerticalPositionAfter == positionCenter -> {
//                paramsInput.addRule(RelativeLayout.BELOW, tv_hintBefore.id)
//                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionTop && hintTextVerticalPositionAfter == positionBottom -> {
//                paramsInput.addRule(RelativeLayout.BELOW, tv_hintBefore.id)
//                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintAfter.id)
//                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionCenter && hintTextVerticalPositionAfter == positionTop -> {
//                paramsInput.addRule(RelativeLayout.BELOW, tv_hintAfter.id)
//                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionCenter && hintTextVerticalPositionAfter == positionCenter -> {
//                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionCenter && hintTextVerticalPositionAfter == positionBottom -> {
//                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintAfter.id)
//                paramsHintBefore.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionBottom && hintTextVerticalPositionAfter == positionTop -> {
//                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintBefore.id)
//                paramsInput.addRule(RelativeLayout.BELOW, tv_hintAfter.id)
//                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionBottom && hintTextVerticalPositionAfter == positionCenter -> {
//                paramsInput.addRule(RelativeLayout.ABOVE, tv_hintBefore.id)
//                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
//            }
//
//            hintTextVerticalPositionBefore == positionBottom && hintTextVerticalPositionAfter == positionBottom -> {
//                when {
//                    hintTextSizeBefore > hintTextSizeAfter -> paramsInput.addRule(RelativeLayout.ABOVE, tv_hintBefore.id)
//                    else -> paramsInput.addRule(RelativeLayout.ABOVE, tv_hintAfter.id)
//                }
//                paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
//                paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
//            }
//        }
        et_input.layoutParams = paramsInput
        tv_hintBefore.layoutParams = paramsHintBefore
        tv_hintAfter.layoutParams = paramsHintAfter
        tv_hint.layoutParams = paramsHintBefore
    }

    private fun setHorizontalPosition() {
        val paramsHintBefore = tv_hintBefore.layoutParams as RelativeLayout.LayoutParams
        when (hintTextHorizontalPositionBefore) {
            left, positionStart -> paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
            right, positionEnd -> paramsHintBefore.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            else -> paramsHintBefore.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        }
        tv_hintBefore.layoutParams = paramsHintBefore

        val paramsHintAfter = tv_hintAfter.layoutParams as RelativeLayout.LayoutParams
        when (hintTextHorizontalPositionAfter) {
            left, positionStart -> paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE)
            right, positionEnd -> paramsHintAfter.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
            else -> paramsHintAfter.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        }
        tv_hintAfter.layoutParams = paramsHintAfter
        tv_hint.layoutParams = paramsHintBefore
    }

    private var animatorSet = AnimatorSet()
    private fun setAnim(duration: Long, isFocused: Boolean) {
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

        val hintSizeDifference = tv_hintBefore.textSize - tv_hintAfter.textSize
        val hintXDifference = tv_hintBefore.x - tv_hintAfter.x
        val hintYDifference = tv_hintBefore.y - tv_hintAfter.y

        val scaleAnimator = if (isFocused) ValueAnimator.ofFloat(0f, 1f) else ValueAnimator.ofFloat(1f, 0f)
        scaleAnimator.duration = duration
        scaleAnimator.interpolator = DecelerateInterpolator()
        scaleAnimator.addUpdateListener {
            val floatValue = it.animatedValue as Float

            tv_hint.textSize = tv_hintBefore.textSize - hintSizeDifference * floatValue
//            tv_hint.textSize = hintSizeDifference * floatValue + smallSize

            tv_hint.x = tv_hintBefore.x - hintXDifference * floatValue
            tv_hint.y = tv_hintBefore.y - hintYDifference * floatValue
        }

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