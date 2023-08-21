package cn.edu.fzu.mytoolbox.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout

// 自定义 View 继承自 LinearLayout
class MyMarqueeView(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    // 初始化一个属性动画对象
    private val animator = ValueAnimator()

    // 定义一些常量，比如滚动的间隔时间和停留时间
    companion object {
        const val INTERVAL_TIME = 8000L // 8 秒
        const val STAY_TIME = 2000L // 2 秒
    }

    init {
        // 设置 LinearLayout 的方向为垂直
        orientation = VERTICAL
        // 设置属性动画的值为 0 到 1
        animator.setFloatValues(0f, 1f)
        // 设置属性动画的监听器
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                // 动画结束后，延迟一段时间再开始下一次动画
                postDelayed({ startAnimation() }, STAY_TIME)
            }
        })
    }

    // 计算 LinearLayout 的总高度和每个子 View 的偏移量
    private var totalHeight = 0
    private var offsetHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 获取子 View 的数量
        val childCount = childCount
        if (childCount > 0) {
            // 获取第一个子 View 的高度
            val childHeight = getChildAt(0).measuredHeight
            // 计算 LinearLayout 的总高度为子 View 的高度乘以数量
            totalHeight = childHeight * childCount
            // 计算每个子 View 的偏移量为子 View 的高度乘以负一
            offsetHeight = -childHeight
            // 设置 LinearLayout 的高度为第一个子 View 的高度
            setMeasuredDimension(measuredWidth, childHeight)
        }
    }

    // 根据属性动画的当前值，设置每个子 View 的位置，并刷新布局
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        // 获取属性动画的当前值
        val value = animator.animatedValue as Float
        // 遍历每个子 View
        for (i in 0 until childCount) {
            // 获取当前的子 View
            val child = getChildAt(i)
            // 计算当前的子 View 的垂直偏移量，根据属性动画的值和每个子 View 的偏移量来计算
            val top = (offsetHeight * i + offsetHeight * value).toInt()
            // 设置当前的子 View 的位置，左右不变，上下根据偏移量变化
            child.layout(child.left, top, child.right, top + child.measuredHeight)
        }
        // 刷新布局
        requestLayout()
    }

    // 判断是否需要开始或停止属性动画，并设置相应的监听器来控制滚动的间隔时间和停留时间
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isShown && !animator.isStarted) {
            // 如果 View 是可见的，并且属性动画没有开始，就延迟一段时间后开始属性动画
            postDelayed({ startAnimation() }, INTERVAL_TIME)
        } else if (!isShown && animator.isStarted) {
            // 如果 View 是不可见的，并且属性动画已经开始，就停止属性动画
            stopAnimation()
        }
    }

    // 开始属性动画的方法
    private fun startAnimation() {
        if (!animator.isStarted) {
            animator.start()
        }
    }

    // 停止属性动画的方法
    private fun stopAnimation() {
        if (animator.isStarted) {
            animator.cancel()
        }
    }

}
