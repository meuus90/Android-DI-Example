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
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.jakewharton.rxbinding2.view.RxView
import com.libs.meuuslibs.R
import com.libs.meuuslibs.util.ConvertMetrics.Companion.dpToPx
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.widget_meu_edit_text.view.*


class MeUEditText : ConstraintLayout {
    companion object {
        const val posTop = -1
        const val posBottom = 1
        const val posStart = -1
        const val posEnd = 1
        const val posCenter = 0
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

    private var hintTextVerticalPositionBefore: Int = posCenter
    private var hintTextVerticalPositionAfter: Int = posCenter

    private var hintTextHorizontalPositionBefore: Int = posCenter
    private var hintTextHorizontalPositionAfter: Int = posCenter

    private var animateDuration: Long = 100

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

        hintTextVerticalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionBefore, posCenter)
        hintTextVerticalPositionAfter = typedArray.getInt(R.styleable.MeUEditText_hintTextVerticalPositionAfter, hintTextVerticalPositionBefore)

        hintTextHorizontalPositionBefore = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionBefore, posCenter)
        hintTextHorizontalPositionAfter = typedArray.getInt(R.styleable.MeUEditText_hintTextHorizontalPositionAfter, hintTextHorizontalPositionBefore)

        animateDuration = typedArray.getInt(R.styleable.MeUEditText_animateDuration, 100).toLong()

        //EditText
        val editText = typedArray.getString(R.styleable.MeUEditText_editText)
        et_input.setText(editText ?: "")

        val editTextColor = typedArray.getColor(R.styleable.MeUEditText_editTextColor, ContextCompat.getColor(context, R.color.colorBlack))
        et_input.setTextColor(editTextColor)

        val editTextSize = typedArray.getDimensionPixelSize(R.styleable.MeUEditText_editTextSize, dpToPx(context, 15))
        et_input.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize.toFloat())

        setPosition(hintTextVerticalPositionBefore, hintTextHorizontalPositionBefore, tv_hintBefore.id)
        setPosition(hintTextVerticalPositionAfter, hintTextHorizontalPositionAfter, tv_hintAfter.id)

        disposable = RxView.focusChanges(et_input)
//                .throttleFirst(animationDuration, TimeUnit.MILLISECONDS)
                .subscribe {
                    setAnim(it)
                }
        typedArray.recycle()
    }

    private fun setPosition(verticalPos: Int, horizontalPos: Int, id: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(v_root)

        when (verticalPos) {
            posTop -> {
                constraintSet.clear(id, ConstraintSet.BOTTOM)
                constraintSet.connect(id, ConstraintSet.TOP, v_root.id, ConstraintSet.TOP, 0)
                constraintSet.connect(v_root.id, ConstraintSet.TOP, id, ConstraintSet.TOP, 0)
                constraintSet.connect(id, ConstraintSet.BOTTOM, et_input.id, ConstraintSet.TOP, 0)
                constraintSet.connect(et_input.id, ConstraintSet.TOP, id, ConstraintSet.BOTTOM, 0)
            }
            posBottom -> {
                constraintSet.clear(id, ConstraintSet.TOP)
                constraintSet.connect(id, ConstraintSet.BOTTOM, v_root.id, ConstraintSet.BOTTOM, 0)
                constraintSet.connect(v_root.id, ConstraintSet.BOTTOM, id, ConstraintSet.BOTTOM, 0)
                constraintSet.connect(et_input.id, ConstraintSet.BOTTOM, id, ConstraintSet.TOP, 0)
                constraintSet.connect(id, ConstraintSet.TOP, et_input.id, ConstraintSet.BOTTOM, 0)
            }
            else -> {
                constraintSet.clear(id, ConstraintSet.TOP)
                constraintSet.clear(id, ConstraintSet.BOTTOM)
                constraintSet.centerVertically(id, v_root.id)
            }

        }

        when (horizontalPos) {
            posStart -> {
                constraintSet.clear(id, ConstraintSet.END)
                constraintSet.connect(id, ConstraintSet.START, v_root.id, ConstraintSet.START, 0)
                constraintSet.connect(v_root.id, ConstraintSet.START, id, ConstraintSet.START, 0)
            }
            posEnd -> {
                constraintSet.clear(id, ConstraintSet.START)
                constraintSet.connect(id, ConstraintSet.END, v_root.id, ConstraintSet.END, 0)
                constraintSet.connect(v_root.id, ConstraintSet.END, id, ConstraintSet.END, 0)
            }
            else -> {
                constraintSet.clear(id, ConstraintSet.START)
                constraintSet.clear(id, ConstraintSet.END)
                constraintSet.centerHorizontally(id, v_root.id)
            }
        }

        constraintSet.applyTo(v_root)
    }

    private fun setAnim(isFocused: Boolean) {
        val transition = AutoTransition()
        transition.duration = animateDuration
        TransitionManager.beginDelayedTransition(v_root, transition)
//
//        if (isFocused) {
//            v_root.tv_hint.setTextColor(hintTextColorAfter)
//            v_root.tv_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeAfter.toFloat())
//            v_root.background = TransitionDrawable(backgroundDrawablesAfter)
//            (v_root.background as TransitionDrawable).startTransition(animateDuration.toInt())
//
//        } else {
//            v_root.tv_hint.setTextColor(hintTextColorBefore)
//            v_root.tv_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintTextSizeBefore.toFloat())
//            v_root.background = TransitionDrawable(backgroundDrawablesBefore)
//            (v_root.background as TransitionDrawable).startTransition(animateDuration.toInt())
//
//        }


        val animatorSet = AnimatorSet()
        val colorAnimator = ValueAnimator()
        if (isFocused)
            colorAnimator.setIntValues(hintTextColorBefore, hintTextColorAfter)
        else
            colorAnimator.setIntValues(hintTextColorAfter, hintTextColorBefore)
        colorAnimator.setEvaluator(ArgbEvaluator())
        colorAnimator.duration = animateDuration
        colorAnimator.interpolator = DecelerateInterpolator()
        colorAnimator.addUpdateListener {
            tv_hint.setTextColor(it.animatedValue as Int)
        }

        val scaleAnimator =
                if (isFocused) ValueAnimator.ofFloat(tv_hintBefore.textSize, tv_hintAfter.textSize)
                else ValueAnimator.ofFloat(tv_hintAfter.textSize, tv_hintBefore.textSize)

        scaleAnimator.duration = animateDuration
        scaleAnimator.addUpdateListener {
            val floatValue = it.animatedValue as Float
            tv_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, floatValue)

            if (isFocused) {
                val params = tv_hintAfter.layoutParams as ConstraintLayout.LayoutParams
                params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                tv_hint.layoutParams = params
            } else {
                val params = tv_hintBefore.layoutParams as ConstraintLayout.LayoutParams
                params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                tv_hint.layoutParams = params
            }
        }

        animatorSet.playTogether(colorAnimator, scaleAnimator)
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                if (isFocused) {
                    v_root.background = TransitionDrawable(backgroundDrawablesAfter)
                    (v_root.background as TransitionDrawable).startTransition(animateDuration.toInt())
                } else {
                    v_root.background = TransitionDrawable(backgroundDrawablesBefore)
                    (v_root.background as TransitionDrawable).startTransition(animateDuration.toInt())
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

    private var disposable: Disposable? = null
    override fun onDetachedFromWindow() {
        if (disposable != null && !disposable!!.isDisposed)
            disposable?.dispose()
        super.onDetachedFromWindow()
    }
}