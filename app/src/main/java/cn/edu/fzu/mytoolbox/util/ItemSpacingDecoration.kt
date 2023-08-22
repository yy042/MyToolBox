package cn.edu.fzu.mytoolbox.util

import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.fzu.mytoolbox.util.Util.dpToPx

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        val parentWidth3=parent.measuredWidth
        println("parentWidth3 is $parentWidth3")

        if(position!=0){
            outRect.left = spacing
        }

        //如果item个数小于4个
        if(itemCount<4){
            // 设置item的宽度
            val totalSpacing = spacing * (itemCount - 1)
            val itemWidth = (parent.measuredWidth - totalSpacing) / itemCount
            val layoutParams = view.layoutParams
            layoutParams.width = itemWidth
            layoutParams.height = itemWidth * 90 / 85 // 保持宽高比为 85:90
            view.layoutParams = layoutParams

            //文字一起放大
/*            val textViews = ArrayList<TextView>()
            if (view is ViewGroup) {
                val count = view.childCount
                for (i in 0 until count) {
                    val childView = view.getChildAt(i)
                    if (childView is TextView) {
                        textViews.add(childView)
                    }
                }
            }

            view.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                val viewWidth = view.width

                textViews.forEach { textView ->
                    val initialTextSize = textView.textSize
                    val newScaledTextSize = initialTextSize * (viewWidth.toFloat() / textView.width)

                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newScaledTextSize)

                }
            }*/

        }else{
            // 设置item的宽度
            val totalSpacing = spacing * 3
            val itemWidth = (parent.measuredWidth - totalSpacing) / 3.5
            val layoutParams = view.layoutParams
            layoutParams.width = itemWidth.toInt()
            layoutParams.height = (itemWidth * 90 / 85).toInt() // 保持宽高比为 85:90
            view.layoutParams = layoutParams
        }
    }
}