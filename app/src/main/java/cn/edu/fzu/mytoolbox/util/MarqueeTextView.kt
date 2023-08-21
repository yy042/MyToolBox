package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet

// 自定义一个MarqueeTextView类，继承自TextView
class MarqueeTextView : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun isFocused(): Boolean {
        return true
    }
}
