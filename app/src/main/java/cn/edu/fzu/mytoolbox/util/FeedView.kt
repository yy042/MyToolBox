package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.adapter.EmbeddedViewPagerAdapter
import cn.edu.fzu.mytoolbox.databinding.TabCustomBinding
import cn.edu.fzu.mytoolbox.databinding.ViewFeedBinding
import cn.edu.fzu.mytoolbox.entity.GetFeedTabData
import cn.edu.fzu.mytoolbox.entity.GetFeedTabData.TAB_TYPE
import cn.edu.fzu.mytoolbox.fragment.MultiViewWaterfallFragment
import cn.edu.fzu.mytoolbox.fragment.ScratchCardFragment
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

        // 设置ViewPager2
        val pagerAdapter= EmbeddedViewPagerAdapter(context as FragmentActivity)
        // 设置ViewPager2的adapter属性为pagerAdapter
        binding.feedViewPager.adapter = pagerAdapter
        // 设置ViewPager2的预加载数量，以避免每次切换都重新加在Fragment
        binding.feedViewPager.offscreenPageLimit = tabList.size

        val tabLayout = binding.feedTabLayout
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        // 遍历tabList，添加fragment
        for (tab in tabList) {
            pagerAdapter.addFragment(MultiViewWaterfallFragment())
        }

        // 添加一个监听器，根据tab的选中状态，动态地修改tab的文字颜色和大小
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 当tab被选中时，获取tab的customView
                val view = tab.customView
                // 获取视图中的TextView组件
                val tvTabText = view?.findViewById<TextView>(R.id.tabName)

                if (tvTabText != null) {
                    // 将TextView的textSize设置为17sp
                    tvTabText.textSize = 17f
                    // 将TextView的textColor设置为黑色
                    tvTabText.setTextColor(Color.BLACK)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 当tab未被选中时，获取tab的customView
                val view = tab.customView
                // 获取视图中的TextView组件
                val tvTabText = view?.findViewById<TextView>(R.id.tabName)

                if (tvTabText != null) {
                    // 将TextView的textSize设置为16sp
                    tvTabText.textSize = 16f
                    // 将TextView的textColor设置为灰色
                    tvTabText.setTextColor(Color.GRAY)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 当tab被重复选中时
                // 设置取消红点，目前不起作用
                val view = tab.customView
                val badge=view?.findViewById<TextView>(R.id.tabBadge)
                if(badge != null){
                    badge.visibility=View.GONE
                }
            }
        })

        // 创建一个 TabConfigurationStrategy 对象，实现 configureTab 方法
        val tabConfigurationStrategy = object : TabLayoutMediator.TabConfigurationStrategy {

            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                // 根据position获取对应的TabListBean对象
                val tabData = tabList[position]
                // 根据tabType设置tabItem的显示类型，1：显示标题 2：显示图标
                when (tabData.tabType) {
                    TAB_TYPE.TITLE -> {
                        // 加载自定义视图的布局文件
                        val view = LayoutInflater.from(context).inflate(R.layout.tab_custom, null)
                        // 获取视图中的TextView组件，并设置其文本为tabName
                        view.findViewById<TextView>(R.id.tabName).text = tabData.tabName
                        // 隐藏视图中的ImageView组件
                        view.findViewById<ImageView>(R.id.tabIcon).visibility = View.GONE
                        // 获取视图中的小红点组件，并根据redFlag设置其可见性，0否 1是
                        view.findViewById<TextView>(R.id.tabBadge).visibility =
                            if (tabData.redFlag == "1") View.VISIBLE else View.GONE
                        // 设置tab的customView属性为view
                        tab.customView = view
                    }
                    TAB_TYPE.ICON -> {
                        // 加载自定义视图的布局文件
                        val view = LayoutInflater.from(context).inflate(R.layout.tab_custom, null)
                        // 获取视图中的ImageView组件，并使用Glide来加载网络图片，并设置给其drawable属性
                        Glide.with(context).load(tabData.tabIcon).fitCenter().into(view.findViewById<ImageView>(R.id.tabIcon))
                        // 隐藏视图中的TextView组件
                        view.findViewById<TextView>(R.id.tabName).visibility = View.GONE
                        // 获取视图中的小红点组件，并根据redFlag设置其可见性，0否 1是
                        view.findViewById<TextView>(R.id.tabBadge).visibility =
                            if (tabData.redFlag == "1") View.VISIBLE else View.GONE
                        // 设置tab的customView属性为view
                        tab.customView = view
                    }
                }
            }
        }

        // 创建一个 TabLayoutMediator 对象，将 tabLayout, binding.feedViewPager 和 tabConfigurationStrategy 作为参数传递
        val tabLayoutMediator = TabLayoutMediator(tabLayout, binding.feedViewPager, tabConfigurationStrategy)

        // 调用 attach 方法，建立 TabLayout 和 ViewPager2 的联动
        tabLayoutMediator.attach()

      /*  // 设置TabLayout的选中监听器，当用户点击某个tab时触发相应的逻辑
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

                    }
                    "2" -> {
                        // 显示wap页面，可以使用WebView或其他浏览器控件实现，并传入selectedTab.link作为跳转链接

                    }
                    "3" -> {
                        // 显示原生关注页，可以自定义一个关注页控件实现，并传入selectedTab.tagList作为标签列表

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
        })*/
    }


}