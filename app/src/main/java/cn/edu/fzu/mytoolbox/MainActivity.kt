package cn.edu.fzu.mytoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import cn.edu.fzu.mytoolbox.adapter.ViewPagerAdapter
import cn.edu.fzu.mytoolbox.databinding.ActivityMainBinding
import cn.edu.fzu.mytoolbox.fragment.RecyclerViewFragment
import com.google.android.material.tabs.TabLayoutMediator

// 定义一个顶层变量
var viewPager: ViewPager2? = null


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // 定义一个数组，存放四个fragment的标题
    private val titles = arrayOf("网格", "Fragment 2", "Fragment 3", "Fragment 4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 创建一个ViewPagerAdapter对象，传入supportFragmentManager和lifecycle
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        // 给adapter添加四个fragment对象，可以自定义fragment的内容和布局
        adapter.addFragment(RecyclerViewFragment())
        adapter.addFragment(RecyclerViewFragment())
        adapter.addFragment(RecyclerViewFragment())

        // 给viewPager设置adapter
        binding.mainViewPager.adapter = adapter
        viewPager=binding.mainViewPager

        // 使用TabLayoutMediator来关联TabLayout和ViewPager2，传入tabLayout, viewPager和一个回调函数
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, position ->
            // 在回调函数中，给每个tab设置标题
            tab.text = titles[position]
        }.attach() // 调用attach方法来完成关联


    }


}