package cn.edu.fzu.mytoolbox.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// 定义一个ViewPagerAdapter类，继承自FragmentStateAdapter
class EmbeddedViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    // 定义一个列表，用来存放fragment对象
    private val fragments = mutableListOf<Fragment>()

    // 定义一个方法，用来向列表中添加fragment对象
    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }

    // 重写getItemCount方法，返回列表的大小
    override fun getItemCount(): Int {
        return fragments.size
    }

    // 重写createFragment方法，返回列表中指定位置的fragment对象
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}