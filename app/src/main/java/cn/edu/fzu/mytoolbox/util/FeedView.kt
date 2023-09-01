package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.adapter.EmbeddedViewPagerAdapter
import cn.edu.fzu.mytoolbox.databinding.TabCustomBinding
import cn.edu.fzu.mytoolbox.databinding.ViewFeedBinding
import cn.edu.fzu.mytoolbox.entity.GetFeedTabData
import cn.edu.fzu.mytoolbox.fragment.MultiViewWaterfallFragment
import cn.edu.fzu.mytoolbox.fragment.ScratchCardFragment
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class FeedView (context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs){

    //初始化绑定类
    private val binding = ViewFeedBinding.inflate(LayoutInflater.from(context), this, true)

    init{
        // 设置ViewPager2
        val pagerAdapter= EmbeddedViewPagerAdapter(context as FragmentActivity)
        // 设置ViewPager2的adapter属性为pagerAdapter
        binding.feedViewPager.adapter = pagerAdapter

        // 从本地json文件中读取json字符串
        val json = context.assets.open("tab.json").bufferedReader().use { it.readText() }
        // 使用Gson对象将json字符串转换为TabListBean对象列表
        val gson = Gson()
        val feedTabData = gson.fromJson(json, GetFeedTabData::class.java)
        val tabList: List<GetFeedTabData.TabListBean> = feedTabData.tabList

        val tabLayout = binding.feedTabLayout
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        // 遍历tabList，为每个TabListBean对象创建一个对应的TabItem，并添加到TabLayout中
        for (tab in tabList) {
            // 创建一个TabLayout.Tab对象
            val tabItem = tabLayout.newTab()
            // 根据tabType设置tabItem的显示类型，1：显示标题 2：显示图标
            when (tab.tabType) {
                "1" -> {
                    // 加载自定义视图的布局文件
                    val view = LayoutInflater.from(context).inflate(R.layout.tab_custom, null)
                    // 获取视图中的TextView组件
                    val tvTabText = view.findViewById<TextView>(R.id.tabName)
                    // 设置TextView的text属性为tabName
                    tvTabText.text = tab.tabName
                    // 隐藏tabIcon
                    val ivTabIcon = view.findViewById<ImageView>(R.id.tabIcon)
                    ivTabIcon.visibility = GONE
                    // 获取小红点
                    val tvTabBadge = view.findViewById<TextView>(R.id.tabBadge)
                    // 根据redFlag设置tabItem是否显示红点，0否 1是
                    if(tab.redFlag == "1"){
                        tvTabBadge.visibility= VISIBLE
                    }else{
                        tvTabBadge.visibility= GONE
                    }
                    // 设置tabItem的自定义视图为view
                    tabItem.customView = view
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
                            // tabItem.icon = drawable

                            // 使用自定义视图来设置图标
                            // 加载自定义视图的布局文件
                            val view = LayoutInflater.from(context).inflate(R.layout.tab_custom, null)
                            // 获取小红点
                            val tvTabBadge = view.findViewById<TextView>(R.id.tabBadge)
                            // 根据redFlag设置tabItem是否显示红点，0否 1是
                            if(tab.redFlag == "1"){
                                tvTabBadge.visibility= VISIBLE
                            }else{
                                tvTabBadge.visibility= GONE
                            }
                            // 获取视图中的ImageView组件
                            val ivTabIcon = view.findViewById<ImageView>(R.id.tabIcon)
                            // 获取tabName
                            val tvTabText = view.findViewById<TextView>(R.id.tabName)
                            // 设置ImageView的drawable属性为图标
                            ivTabIcon.setImageDrawable(drawable)
                            // 设置ImageView的scaleType属性为fitCenter或centerInside
                            ivTabIcon.scaleType = ImageView.ScaleType.FIT_CENTER
                            // 设置ImageView的layout_height属性为文字的尺寸或其他合适的值
                            ivTabIcon.layoutParams.height = tvTabText.layoutParams.height
                            // 隐藏tabName
                            tvTabText.visibility= GONE
                            // 设置tabItem的自定义视图为view
                            tabItem.customView = view
                        } catch (e: Exception) {
                            // 处理可能发生的异常，例如网络错误，图片格式错误等
                            e.printStackTrace()
                        }
                    }).start()
                }
            }

            // 将tabItem添加到TabLayout中
            tabLayout.addTab(tabItem)
            pagerAdapter.addFragment(MultiViewWaterfallFragment())
        }

        // 设置TabLayout的选中监听器，当用户点击某个tab时触发相应的逻辑
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 设置ViewPager2的当前页面为tab对应的位置
                binding.feedViewPager.setCurrentItem(tab.position, true)
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
        binding.feedViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 设置TabLayout的当前tab为position对应的tab，并平滑滚动
                tabLayout.setScrollPosition(position, 0f, true)
                // 遍历所有的tabItem
                for (i in 0 until tabLayout.tabCount) {
                    val tab = tabLayout.getTabAt(i)
                    // 设置选中的tab文字变黑
                    val view = tab?.customView
                    // 获取视图中的TextView组件
                    val tvTabText = view?.findViewById<TextView>(R.id.tabName)
                    if (tab != null) {
                        if (tab.position == position) {
                            // 如果TextView不为空
                            if (tvTabText != null) {
                                // 将TextView的textSize设置为17sp
                                tvTabText.textSize = 17f
                                // 将TextView的textColor设置为黑色
                                tvTabText.setTextColor(Color.BLACK)
                            }
                        }
                        else {
                            // 如果TextView不为空
                            if (tvTabText != null) {
                                // 将TextView的textSize设置为16sp
                                tvTabText.textSize = 16f
                                // 将TextView的textColor设置为灰色
                                tvTabText.setTextColor(Color.GRAY)
                            }
                        }
                    }

                }

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