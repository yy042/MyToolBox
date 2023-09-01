package cn.edu.fzu.mytoolbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import cn.edu.fzu.mytoolbox.adapter.ViewPagerAdapter
import cn.edu.fzu.mytoolbox.databinding.ActivityRecyclerViewBinding
import cn.edu.fzu.mytoolbox.fragment.GridRecyclerViewFragment
import cn.edu.fzu.mytoolbox.fragment.RecyclerViewFragment
import cn.edu.fzu.mytoolbox.fragment.WaterfallFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar

// 定义一个顶层变量
var viewPager: ViewPager2? = null

class RecyclerViewActivity : AppCompatActivity() {
    // 定义一个数组，存放四个fragment的标题
    private val titles = arrayOf("水平&垂直RecyclerView", "网格", "瀑布流", "Fragment 4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //设置沉浸式状态栏
        ImmersionBar.with(this)
            .statusBarDarkFont(true) //设置状态栏字体为深色
            .init()

        // 创建一个ViewPagerAdapter对象，传入supportFragmentManager和lifecycle
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        // 给adapter添加四个fragment对象，可以自定义fragment的内容和布局
        adapter.addFragment(RecyclerViewFragment())
        adapter.addFragment(GridRecyclerViewFragment())
        adapter.addFragment(WaterfallFragment())

        // 给viewPager设置adapter
        binding.rvActivityViewPager.adapter = adapter
        viewPager=binding.rvActivityViewPager

        // 使用TabLayoutMediator来关联TabLayout和ViewPager2，传入tabLayout, viewPager和一个回调函数
        TabLayoutMediator(binding.rvActivityTabLayout, binding.rvActivityViewPager) { tab, position ->
            // 在回调函数中，给每个tab设置标题
            tab.text = titles[position]
        }.attach() // 调用attach方法来完成关联


    }
}