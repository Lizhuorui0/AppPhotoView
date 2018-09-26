package com.zhuorui.iphotoviewlibrary.widget

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.os.Build
import android.support.annotation.AnimatorRes
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import com.zhuorui.iphotoviewlibrary.R


class CircleIndicator : LinearLayout {

    private var mViewpager: ViewPager? = null
    private var mIndicatorMargin = -1
    private var mIndicatorWidth = -1
    private var mIndicatorHeight = -1
    private var mAnimatorResId = R.anim.scale_with_alpha
    private var mAnimatorReverseResId = 0
    private var mIndicatorBackgroundResId = R.drawable.white_radius
    private var mIndicatorUnselectedBackgroundResId = R.drawable.white_radius
    private var mAnimatorOut: Animator? = null
    private var mAnimatorIn: Animator? = null
    private var mImmediateAnimatorOut: Animator? = null
    private var mImmediateAnimatorIn: Animator? = null

    private var mLastPosition = -1

    private val mInternalPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {

            if (mViewpager?.adapter == null || mViewpager?.adapter!!.count <= 0) {
                return
            }
            if (mAnimatorIn != null) {
                if (mAnimatorIn!!.isRunning) {
                    mAnimatorIn?.end()
                    mAnimatorIn?.cancel()
                }
            }
            if (mAnimatorOut != null) {
                if (mAnimatorOut!!.isRunning) {
                    mAnimatorOut?.end()
                    mAnimatorOut?.cancel()
                }
            }
            val currentIndicator: View? = getChildAt(mLastPosition)
            if (mLastPosition >= 0 && currentIndicator != null) {
                currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
                mAnimatorIn?.setTarget(currentIndicator)
                mAnimatorIn?.start()
            }

            val selectedIndicator = getChildAt(position)
            if (selectedIndicator != null) {
                selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId)
                mAnimatorOut?.setTarget(selectedIndicator)
                mAnimatorOut?.start()
            }
            mLastPosition = position
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    // No change
    val dataSetObserver: DataSetObserver = object : DataSetObserver() {

        override fun onChanged() {
            super.onChanged()
            if (mViewpager == null) {
                return
            }

            val newCount = mViewpager!!.adapter!!.count
            val currentCount = childCount

            mLastPosition = when {
                newCount == currentCount -> return
                mLastPosition < newCount -> mViewpager!!.currentItem
                else -> -1
            }

            createIndicators()
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        handleTypedArray(context, attrs)
        checkIndicatorConfig(context)
    }

    @SuppressLint("Recycle")
    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator)
        typedArray.apply {
            mIndicatorWidth = getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1)
            mIndicatorHeight = getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1)
            mIndicatorMargin = getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1)

            mAnimatorResId = getResourceId(R.styleable.CircleIndicator_ci_animator,
                    R.anim.scale_with_alpha)
            mAnimatorReverseResId = getResourceId(R.styleable.CircleIndicator_ci_animator_reverse, 0)
            mIndicatorBackgroundResId = getResourceId(R.styleable.CircleIndicator_ci_drawable,
                    R.drawable.white_radius)
            mIndicatorUnselectedBackgroundResId = getResourceId(R.styleable.CircleIndicator_ci_drawable_unselected,
                    mIndicatorBackgroundResId)

            val orientation = getInt(R.styleable.CircleIndicator_ci_orientation, -1)
            setOrientation(if (orientation == VERTICAL) VERTICAL else HORIZONTAL)

            val gravity = getInt(R.styleable.CircleIndicator_ci_gravity, -1)
            setGravity(if (gravity >= 0) gravity else Gravity.CENTER)

            recycle()
        }

    }

    /**
     * Create and configure Indicator in Java code.
     */
    @SuppressLint("ResourceType")
    fun configureIndicator(indicatorWidth: Int, indicatorHeight: Int, indicatorMargin: Int) {
        configureIndicator(indicatorWidth, indicatorHeight, indicatorMargin,
                R.anim.scale_with_alpha, 0, R.drawable.white_radius, R.drawable.white_radius)
    }

    private fun configureIndicator(indicatorWidth: Int, indicatorHeight: Int, indicatorMargin: Int,
                                   @AnimatorRes animatorId: Int, @AnimatorRes animatorReverseId: Int,
                                   @DrawableRes indicatorBackgroundId: Int,
                                   @DrawableRes indicatorUnselectedBackgroundId: Int) {

        mIndicatorWidth = indicatorWidth
        mIndicatorHeight = indicatorHeight
        mIndicatorMargin = indicatorMargin

        mAnimatorResId = animatorId
        mAnimatorReverseResId = animatorReverseId
        mIndicatorBackgroundResId = indicatorBackgroundId
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundId

        checkIndicatorConfig(context)
    }

    private fun checkIndicatorConfig(context: Context) {
        mIndicatorWidth = when (mIndicatorWidth < 0) {
            true -> dip2px(DEFAULT_INDICATOR_WIDTH.toFloat())
            else -> mIndicatorWidth
        }
        mIndicatorHeight = when (mIndicatorHeight < 0) {
            true -> dip2px(DEFAULT_INDICATOR_WIDTH.toFloat())
            else -> mIndicatorHeight
        }
        mIndicatorMargin = when (mIndicatorMargin < 0) {
            true -> dip2px(DEFAULT_INDICATOR_WIDTH.toFloat())
            else -> mIndicatorMargin
        }
        mAnimatorResId = when (mAnimatorResId == 0) {
            true -> R.anim.scale_with_alpha
            else -> mAnimatorResId
        }

        mAnimatorOut = createAnimatorOut(context)
        mImmediateAnimatorOut = createAnimatorOut(context)
        mImmediateAnimatorOut!!.duration = 0

        mAnimatorIn = createAnimatorIn(context)
        mImmediateAnimatorIn = createAnimatorIn(context)
        mImmediateAnimatorIn!!.duration = 0

        mIndicatorBackgroundResId = when (mIndicatorBackgroundResId == 0) {
            true -> R.drawable.white_radius
            else -> mIndicatorBackgroundResId
        }

        mIndicatorUnselectedBackgroundResId = when (mIndicatorUnselectedBackgroundResId == 0) {
            true -> mIndicatorBackgroundResId
            else -> mIndicatorUnselectedBackgroundResId
        }

    }

    @SuppressLint("ResourceType")
    private fun createAnimatorOut(context: Context): Animator {
        return AnimatorInflater.loadAnimator(context, mAnimatorResId)
    }

    @SuppressLint("ResourceType")
    private fun createAnimatorIn(context: Context): Animator {
        val animatorIn: Animator
        when (mAnimatorReverseResId) {
            0 -> {
                animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorResId)
                animatorIn.interpolator = ReverseInterpolator()
            }
            else -> {
                animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId)
            }
        }
        return animatorIn
    }

    fun setViewPager(viewPager: ViewPager) {
        mViewpager = viewPager
        if (mViewpager != null && mViewpager?.adapter != null) {
            mLastPosition = -1
            createIndicators()
            mViewpager?.removeOnPageChangeListener(mInternalPageChangeListener)
            mViewpager?.addOnPageChangeListener(mInternalPageChangeListener)
            mInternalPageChangeListener.onPageSelected(mViewpager!!.currentItem)
        }
    }


    @Deprecated("User ViewPager addOnPageChangeListener")
    fun setOnPageChangeListener(onPageChangeListener: ViewPager.OnPageChangeListener) {
        if (mViewpager == null) {
            throw NullPointerException("can not find Viewpager , setViewPager first")
        }
        mViewpager?.removeOnPageChangeListener(onPageChangeListener)
        mViewpager?.addOnPageChangeListener(onPageChangeListener)
    }

    private fun createIndicators() {
        removeAllViews()
        val count = mViewpager?.adapter!!.count
        if (count <= 0) {
            return
        }
        val currentItem = mViewpager?.currentItem
        val orientation = orientation

        for (i in 0 until count) {
            when (currentItem) {
                i -> addIndicator(orientation, mIndicatorBackgroundResId, mImmediateAnimatorOut!!)
                else -> addIndicator(orientation, mIndicatorUnselectedBackgroundResId, mImmediateAnimatorIn!!)
            }
        }
    }

    private fun addIndicator(orientation: Int, @DrawableRes backgroundDrawableId: Int,
                             animator: Animator) {
        if (animator.isRunning) {
            animator.end()
            animator.cancel()
        }

        val indicator = View(context)
        indicator.setBackgroundResource(backgroundDrawableId)
        addView(indicator, mIndicatorWidth, mIndicatorHeight)
        val lp = indicator.layoutParams as LayoutParams
        when (orientation) {
            HORIZONTAL -> {
                lp.leftMargin = mIndicatorMargin
                lp.rightMargin = mIndicatorMargin
            }
            else -> {
                lp.topMargin = mIndicatorMargin
                lp.bottomMargin = mIndicatorMargin
            }
        }
        indicator.layoutParams = lp

        animator.setTarget(indicator)
        animator.start()
    }

    private inner class ReverseInterpolator : Interpolator {

        override fun getInterpolation(value: Float): Float {
            return Math.abs(1.0f - value)
        }
    }

    fun dip2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    companion object {

        private const val DEFAULT_INDICATOR_WIDTH = 5
    }
}
