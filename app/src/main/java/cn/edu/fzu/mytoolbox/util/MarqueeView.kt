package cn.edu.fzu.mytoolbox.util

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.edu.fzu.mytoolbox.R
import org.jetbrains.annotations.Nullable


/**
 * 上下滚动的 textView
 */
@SuppressLint("MissingInflatedId")
class MarqueeView @JvmOverloads constructor(
    context: Context?,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private val mBannerTV1: MarqueeTextView
    private val mBannerTV2: TextView
    @get:JvmName("getMqHandler")
    private var handler: Handler
    private var isShow = false
    private var startY1 = 0
    private var endY1 = 0
    private var startY2 = 0
    private var endY2 = 0
    private lateinit var  runnable: Runnable
    var list: List<String>? = null
        private set
    private var position = 0
    private val offsetY = 100
    private var hasPostRunnable = false

    init {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_marquee_view, this)
        mBannerTV1 = view.findViewById(R.id.marquee_name)
        mBannerTV2 = view.findViewById(R.id.marquee_desc)
        handler = Handler()
        runnable = Runnable {
            isShow = !isShow
            if (position == list!!.size - 1) {
                position = 0
            }
            if (isShow) {
                mBannerTV1.text = list!![position++]
                mBannerTV2.text = list!![position]
            } else {
                mBannerTV2.text = list!![position++]
                mBannerTV1.text = list!![position]
            }
            startY1 = if (isShow) 0 else offsetY
            endY1 = if (isShow) -offsetY else 0
            ObjectAnimator.ofFloat(mBannerTV1, "translationY", startY1.toFloat(), endY1.toFloat())
                .setDuration(300).start()
            startY2 = if (isShow) offsetY else 0
            endY2 = if (isShow) 0 else -offsetY
            ObjectAnimator.ofFloat(mBannerTV2, "translationY", startY2.toFloat(), endY2.toFloat())
                .setDuration(300).start()
            handler.postDelayed(runnable, 8000)
        }
    }



    fun setList(list: MutableList<String>) {
        this.list = list

        //处理最后一条数据切换到第一条数据 太快的问题
        if (list.size > 1) {
            list.add(list[0])
        }
    }

    fun startScroll() {
        mBannerTV1.text = list!![0]
        if (list!!.size > 1) {
            if (!hasPostRunnable) {
                hasPostRunnable = true
                //处理第一次进入 第一条数据切换第二条 太快的问题
                handler.postDelayed(runnable, 8000)
            }
        } else {
            //只有一条数据不进行滚动
            hasPostRunnable = false
            //            mBannerTV1.setText(list.get(0));
        }
    }

    fun stopScroll() {
        handler.removeCallbacks(runnable)
        hasPostRunnable = false
    }
}