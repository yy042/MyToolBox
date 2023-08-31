package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import cn.edu.fzu.mytoolbox.databinding.ViewFeedBinding
import cn.edu.fzu.mytoolbox.entity.GetFeedTabData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson

class FeedView (context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs){

    //初始化绑定类
    private val binding = ViewFeedBinding.inflate(LayoutInflater.from(context), this, true)

    init{
        // 从本地json文件中读取json字符串
        val json = context.assets.open("tab.json").bufferedReader().use { it.readText() }
        // 使用Gson对象将json字符串转换为TabListBean对象列表
        val gson = Gson()
        val feedTabData = gson.fromJson(json, GetFeedTabData::class.java)
        val tabList: List<GetFeedTabData.TabListBean> = feedTabData.tabList

        val tabLayout = binding.feedTabLayout
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

/*
        val tabItemBinding=TabCustomBinding.inflate(LayoutInflater.from(context))
        val tabIcon = tabItemBinding.tabIcon
        val tabName = tabItemBinding.tabName
*/

        // 遍历tabList，为每个TabListBean对象创建一个对应的TabItem，并添加到TabLayout中
        for (tab in tabList) {
            // 创建一个TabLayout.Tab对象
            val tabItem = tabLayout.newTab()
            // 根据tabType设置tabItem的显示类型，1：显示标题 2：显示图标
            when (tab.tabType) {
                "1" -> {
                    // 设置tabItem的text属性为tabName
                    tabItem.text = tab.tabName
                }
                "2" -> {
                    // 设置tabItem的icon属性为tabIcon，如果是网络图片，可以使用Glide或其他图片加载库加载
                    // 创建一个后台线程来执行Glide的同步方法
                    Thread(Runnable {
                        try {
                            // 使用Glide的submit方法来返回一个Future对象，表示图片加载的结果
                            val future = Glide.with(context).load(tab.tabIcon).submit()
                            // 使用Future对象的get方法来获取一个Drawable对象，表示图片本身
                            val drawable = future.get()
                            // 在后台线程上设置图标
                            tabItem.icon = drawable
                        } catch (e: Exception) {
                            // 处理可能发生的异常，例如网络错误，图片格式错误等
                            e.printStackTrace()
                        }
                    }).start()
                }
            }
            // 根据redFlag设置tabItem是否显示红点，0否 1是
            if (tab.redFlag == "1") {
                // 设置tabItem的badge为红点，并设置其位置和颜色等属性
                // 要使用安卓原生的badgeDrawable的话，必须将应用主题设置为继承自Theme.MaterialComponents的主题或其子类
                val badgeDrawable = tabItem.orCreateBadge
                badgeDrawable.badgeGravity= BadgeDrawable.TOP_END
                badgeDrawable.backgroundColor = Color.RED
            }

            // 将tabItem添加到TabLayout中
            tabLayout.addTab(tabItem)
        }

        // 设置TabLayout的选中监听器，当用户点击某个tab时触发相应的逻辑
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 获取当前选中的tab对应的TabListBean对象
                val selectedTab = tabList[tab.position]
                // 根据type设置tab栏显示内容类型，1.显示feed流原生列表 2：显示wap页面 3：10.0新增显示原生关注页
                when (selectedTab.type) {
                    "1" -> {
                        // 显示feed流原生列表，可以使用RecyclerView或其他列表控件实现，并传入selectedTab.order作为查询参数
                        showFeedList(selectedTab.order)
                    }
                    "2" -> {
                        // 显示wap页面，可以使用WebView或其他浏览器控件实现，并传入selectedTab.link作为跳转链接
                        showWebPage(selectedTab.link)
                    }
                    "3" -> {
                        // 显示原生关注页，可以自定义一个关注页控件实现，并传入selectedTab.tagList作为标签列表
                        showFollowPage(selectedTab.tagList)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 当某个tab取消选中时，可以做一些清理或回收的操作
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 当某个tab再次选中时，可以做一些刷新或重置的操作
            }
        })

    }

    // 定义一些辅助方法，用于实现上述的逻辑，这里只是简单的示例，具体的实现可以根据需求修改
    private fun showFeedList(order: String) {
        // 使用RecyclerView或其他列表控件显示feed流原生列表，并传入order作为查询参数
    }

    private fun showWebPage(link: String) {
        // 使用WebView或其他浏览器控件显示wap页面，并传入link作为跳转链接
    }

    private fun showFollowPage(tagList: List<GetFeedTabData.TabListBean.TagListBean>) {
        // 自定义一个关注页控件显示原生关注页，并传入tagList作为标签列表
    }

}