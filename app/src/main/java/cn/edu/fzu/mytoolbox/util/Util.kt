package cn.edu.fzu.mytoolbox.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

@SuppressLint("WrongConstant")
fun <T> setupRecyclerView(
    recyclerView: RecyclerView, //传入recyclerView对象
    adapter: BaseQuickAdapter<T, BaseViewHolder>, //传入adapter对象
    dataList: List<T>, //传入数据列表
    orientation: Int = LinearLayoutManager.HORIZONTAL, //传入布局方向，默认为水平
    margin: Int = 0 //传入item间距值，默认为0
) {
    //设置recyclerView的布局管理器
    val layoutManager = LinearLayoutManager(recyclerView.context, orientation, false)
    recyclerView.layoutManager = layoutManager

    //设置recyclerView的adapter
    adapter.setList(dataList)
    recyclerView.adapter = adapter

    //设置recyclerView的item间距
    if (margin > 0) {
        //把item间距值转换成px值，并且除以2
        val distance = margin / 2
        //创建一个ItemDecoration对象，传入间距值
        val itemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                //获取item的位置
                val position = parent.getChildAdapterPosition(view)
                //获取item的总数
                val count = parent.adapter?.itemCount ?: 0
                //如果只有一个item，不设置间距
                if (count == 1) {
                    return
                }
                //根据布局方向，设置不同的边距
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    //如果是第一个item，只设置右边距
                    if (position == 0) {
                        outRect.right = distance
                    }
                    //如果是最后一个item，只设置左边距
                    else if (position == count - 1) {
                        outRect.left = distance
                    }
                    //其他情况，设置左右边距
                    else {
                        outRect.left = distance
                        outRect.right = distance
                    }
                } else {
                    //如果是第一个item，只设置下边距
                    if (position == 0) {
                        outRect.bottom = distance
                    }
                    //如果是最后一个item，只设置上边距
                    else if (position == count - 1) {
                        outRect.top = distance
                    }
                    //其他情况，设置上下边距
                    else {
                        outRect.top = distance
                        outRect.bottom = distance
                    }
                }
            }

        }
        //添加ItemDecoration到recyclerView中
        recyclerView.addItemDecoration(itemDecoration)
    }
}

fun <T> setupGridRecyclerView(
    recyclerView: RecyclerView, //传入recyclerView对象
    adapter: BaseQuickAdapter<T, BaseViewHolder>, //传入adapter对象
    dataList: List<T>, //传入数据列表
    spanCount: Int=3,
    distance:Float=10f,
    orientation: Int = RecyclerView.VERTICAL //传入滑动方向，默认为垂直滑动
) {
    //设置recyclerView的布局管理器
    var layoutManager = GridLayoutManager(recyclerView.context, spanCount, orientation, false)
    recyclerView.layoutManager = layoutManager

    //设置recyclerView的adapter
    adapter.setList(dataList)
    recyclerView.adapter = adapter
    recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(rect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {
            // 获取当前item的位置
            val position = recyclerView.getChildAdapterPosition(view)
            // 获取列数
            val spanCount = layoutManager.spanCount
            // 获取总数
            val itemCount = state.itemCount
            // 计算每个item的左右间距，使得两边对齐，中间间隔为10dp
            val space = ScreenUtils.dp2px(distance, recyclerView.context)
            val left = space * (spanCount - position % spanCount) / spanCount
            val right = space * (position % spanCount + 1) / spanCount
            // 设置每个item的左右间距，顶部间距为10dp，底部间距为0dp（最后一行除外）
            rect.left = left
            rect.right = right
            rect.top = space
            rect.bottom = if (position / spanCount == itemCount / spanCount) space else 0 // 如果是最后一行，则设置底部间距为10dp，否则为0dp
        }

    })

}

//装配瀑布流的适配器和数据
fun <T> setupWaterfall(
    recyclerView: RecyclerView, //传入recyclerView对象
    adapter: BaseQuickAdapter<T, BaseViewHolder>, //传入adapter对象
    dataList: List<T>, //传入数据列表
    orientation: Int = StaggeredGridLayoutManager.VERTICAL, //传入布局方向，默认为水平
    distance:Int=10 // 瀑布流item间距
) {
    //设置recyclerView的布局管理器
    val layoutManager = StaggeredGridLayoutManager(2, orientation)
    recyclerView.layoutManager = layoutManager

    //设置recyclerView的adapter
    adapter.setList(dataList)
    recyclerView.adapter = adapter

    recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(rect: Rect, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {
            // 获取当前item的位置
            val position = recyclerView.getChildAdapterPosition(view)
            // 计算每个item的左右间距，使得间隔为5dp
            val space = (distance/2).dpToPx(recyclerView.context)
            rect.left = space
            rect.right = space
            rect.top = 0
            rect.bottom = distance.dpToPx(recyclerView.context) // 如设置底部间距为10dp
        }
    })

}

//定义一个通用的配置adapter的数据以及和recyclerview绑定的方法
@SuppressLint("WrongConstant")
fun <T> setupSpacingRecyclerView(
    context:Context, //传入上下文
    recyclerView: RecyclerView, //传入recyclerView对象
    adapter: BaseQuickAdapter<T, BaseViewHolder>, //传入adapter对象
    dataList: List<T>, //传入数据列表
    orientation: Int = LinearLayoutManager.HORIZONTAL, //传入布局方向，默认为水平
    spacingInDp:Int=10 //传入间距设置，默认为5
) {
    //设置recyclerView的布局管理器
    val layoutManager = LinearLayoutManager(recyclerView.context, orientation, false)
    recyclerView.layoutManager = layoutManager

    /* // 调用扩展函数，将dp转换为对应的px值
     val spacingInPx = spacingInDp.dpToPx(context)
     // 创建一个CustomSpacingItemDecoration对象，指定间距大小（单位为像素）
     val itemDecoration = CustomSpacingItemDecoration(spacingInPx)

     // 给RecyclerView添加CustomSpacingItemDecoration对象
     recyclerView.addItemDecoration(itemDecoration)*/

    // 调用扩展函数，将dp转换为对应的px值
    val spacingInPx = spacingInDp.dpToPx(context)
    // 创建一个CustomSpacingItemDecoration对象，指定间距大小（单位为像素）
    val itemDecoration = ItemSpacingDecoration(spacingInPx)

    // 给RecyclerView添加CustomSpacingItemDecoration对象
    recyclerView.addItemDecoration(itemDecoration)

    //设置recyclerView的adapter
    adapter.setList(dataList)
    recyclerView.adapter = adapter

}


// 定义一个扩展函数，将Int类型的dp值转换为Int类型的px值
fun Int.dpToPx(context: Context): Int {
    // 获取当前屏幕的密度
    val density = context.resources.displayMetrics.density

    // 根据公式 dp * density + 0.5f 来计算px值，并转换为Int类型
    return (this * density + 0.5f).toInt()
}

// 定义一个扩展函数，将Int类型的dp值转换为Float类型的px值
fun Double.dpToPx(context: Context): Double {
    // 获取当前屏幕的密度
    val density = context.resources.displayMetrics.density

    // 根据公式 dp * density + 0.5f 来计算px值，并转换为Float类型
    return this * density + 0.5f
}

//将状态栏的背景设置为透明
fun transparentStatusBar(window: Window) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    var systemUiVisibility = window.decorView.systemUiVisibility
    systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    window.decorView.systemUiVisibility = systemUiVisibility
    window.statusBarColor = Color.TRANSPARENT //设置状态栏颜色
    //设置状态栏文字颜色
    setStatusBarTextColor(window, NightMode.isNightMode(window.context))
}

//根据当前页面的背景色，给状态栏字体设置亮色或暗色
fun setStatusBarTextColor(window: Window, light: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (light) {
            //白色文字
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else {
            //黑色文字
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }
}

object NightMode {
    fun isNightMode(context: Context): Boolean {
        //Configuration是Android系统提供的一个类，用来描述设备的配置信息，如语言、屏幕大小、夜间模式等
        //获取当前主题的模式
        val mode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        //返回是否为夜间模式
        return mode == Configuration.UI_MODE_NIGHT_YES
    }
}