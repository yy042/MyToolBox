package cn.edu.fzu.mytoolbox

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
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
                val space = ScreenUtils.dp2px(10f,recyclerView.context)
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

    }
}