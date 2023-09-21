package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.absoluteValue
import kotlin.math.sign

class NestedScrollableHost : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f

    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            ViewPager2.ORIENTATION_HORIZONTAL -> child?.canScrollHorizontally(direction) ?: false
            ViewPager2.ORIENTATION_VERTICAL -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val orientation = parentViewPager?.orientation ?: return
        // 如果子View不能在和父ViewPager2方向相同的方向上滚动，则直接返回
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY
            val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL
            // 假设 ViewPager2 的 touch-slop 是子View的两倍
            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {

                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // 手势是垂直的，允许所有父元素拦截触摸事件
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    // 手势是水平的，查询子View是否可以在该方向上移动
                    if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
                        // 子View可以滚动，在该方向上禁止所有父元素拦截触摸事件
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // 子View不能滚动，在该方向上允许所有父元素拦截触摸事件
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
    }
}
