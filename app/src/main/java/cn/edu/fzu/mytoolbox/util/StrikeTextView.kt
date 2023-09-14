package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import cn.edu.fzu.mytoolbox.R

// 定义一个自定义的TextView类，继承自TextView
class StrikeTextView(context: Context, attrs: AttributeSet) :
    AppCompatTextView(context, attrs) {

    // 定义一个布尔型的属性，表示是否绘制删除线
    var strike: Boolean = false
        set(value) {
            field = value
            // 根据属性值，设置或取消画笔的删除线标志
            if (value) {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

    init {
        // 获取自定义属性集合StrikeTextView的引用
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StrikeTextView)
        // 获取并赋值strike属性的值，如果没有设置，默认为false
        strike = typedArray.getBoolean(R.styleable.StrikeTextView_strike, false)
        // 回收typedArray对象，避免内存泄漏
        typedArray.recycle()
    }

    // 重写onDraw方法，在绘制文字前，根据属性值，设置或取消画笔的删除线标志
    override fun onDraw(canvas: Canvas?) {
        if (strike) {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        super.onDraw(canvas)
    }
}
