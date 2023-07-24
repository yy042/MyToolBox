package cn.edu.fzu.mytoolbox

import android.annotation.SuppressLint
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