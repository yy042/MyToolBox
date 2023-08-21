package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.util.AttributeSet
import android.widget.ViewAnimator
import cn.edu.fzu.mytoolbox.R
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils

class MarqueeLayout : ViewAnimator {
    private var delayTime = DEFAULT_TIMER
    private var viewIndex = 0
    private var views: List<View>? = null
    private var started = false //是否已经开始轮播

    //不要把handler定义为静态的伴生对象，而是定义为实例对象的属性
    //不要在构造函数或init方法中初始化handler对象，而是在onAttachedToWindow方法中初始化
    @get:JvmName("getMarqueeHandler")
    private var handler: Handler? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.vertical_marquee_in)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.vertical_marquee_out)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    //在onAttachedToWindow方法中初始化handler对象，并绑定到主线程的消息队列上
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler = Handler(Looper.getMainLooper())
    }

    //在onDetachedFromWindow方法中销毁handler对象，并清除所有的回调和消息
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    private fun startMarquee() {
        if (views != null) {
            if (views!!.size > 1) {
                handler?.postDelayed(object : Runnable {
                    override fun run() {
                        viewIndex++
                        if (viewIndex >= views!!.size) {
                            viewIndex = 0
                        }
                        showNext()
                        handler?.postDelayed(this, delayTime)
                    }
                }, delayTime)
                started = true
            } else if (views!!.size > 0) {
                viewIndex = 0
            } else {
                viewIndex = 0
            }
        } else {
            viewIndex = 0
        }
    }

    /**
     * 获取当前显示的View
     * 修改方法名，避免与父类方法重名
     *
     * @return View
     */
    fun curView(): View? {
        return if (views != null && viewIndex >= 0 && viewIndex < views!!.size) {
            views!![viewIndex]
        } else null
    }

    /**
     * 获取当前显示View的index
     *
     * @return index
     */
    fun curIndex(): Int {
        return viewIndex
    }

    /**
     * 设置轮播的View列表，该方法会自动轮播
     *
     * @param views view列表
     */
    fun setViewList(views: List<View>?) {
        setViewList(views, DEFAULT_TIMER)
    }

    /**
     * 设置轮播的View列表，该方法会自动轮播
     *
     * @param views     view列表
     * @param delayTime 间歇时间
     */
    fun setViewList(views: List<View>?, delayTime: Long) {
        if (views == null || views.isEmpty()) {
            return
        }
        if (delayTime >= 100) {
            //最少100毫秒,否则为默认值
            this.delayTime = delayTime
        }
        this.views = views
        handler?.removeCallbacksAndMessages(null)
        started = false
        post(Runnable {
            for (view in views) {
                addView(view)
            }
            startMarquee()
        })
    }

    //开始倒计时(轮播),在页面可见并且需要自动轮播的时候调用该方法
    fun startTimer() {
        if (started || views == null || views!!.size <= 1) {
            return
        }
        stopTimer()
        startMarquee()
        Log.d("VerticalMarqueeLayout", "VerticalMarqueeLayout startTimer!")
    }

    //停止倒计时(轮播),如果调用过startTimer();在页面不可见的时候调用该方法停止自动轮播
    fun stopTimer() {
        if (handler != null) {
            handler?.removeCallbacksAndMessages(null)
            started = false
            Log.d("VerticalMarqueeLayout", "VerticalMarqueeLayout stopTimer!")
        }
    }

    companion object {
        private const val DEFAULT_TIMER = 4000L //默认时间间隔2秒钟一次滚动切换效果

    //静态Handler对象，防止内存泄漏

    //不要把handler定义为静态的伴生对象，而是定义为实例对象的属性

    //不要在构造函数或init方法中初始化handler对象，而是在onAttachedToWindow方法中初始化

    //在onDetachedFromWindow方法中销毁handler对象，并清除所有的回调和消息

    //private val handler = Handler()
    }
}

