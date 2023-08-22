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

object Util {
    //定义一个通用的配置adapter的数据以及和recyclerview绑定的方法
    @SuppressLint("WrongConstant")
    fun <T> setupRecyclerView(
        recyclerView: RecyclerView, //传入recyclerView对象
        adapter: BaseQuickAdapter<T, BaseViewHolder>, //传入adapter对象
        dataList: List<T>, //传入数据列表
        orientation: Int = LinearLayoutManager.HORIZONTAL //传入布局方向，默认为水平
    ) {
        //设置recyclerView的布局管理器
        val layoutManager = LinearLayoutManager(recyclerView.context, orientation, false)
        recyclerView.layoutManager = layoutManager

        //设置recyclerView的adapter
        adapter.setList(dataList)
        recyclerView.adapter = adapter

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
        orientation: Int = StaggeredGridLayoutManager.VERTICAL //传入布局方向，默认为水平
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
                //val position = recyclerView.getChildAdapterPosition(view)
                // 计算每个item的左右间距，使得间隔为5dp
                val space = 5.dpToPx(recyclerView.context)
                rect.left = space
                rect.right = space
                rect.top = 0
                rect.bottom = 10.dpToPx(recyclerView.context) // 如果是最后一行，则设置底部间距为10dp，否则为0dp
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