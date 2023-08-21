package cn.edu.fzu.mytoolbox.util

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.adapter.EmbeddedViewPagerAdapter
import cn.edu.fzu.mytoolbox.databinding.ViewScratchCardBinding
import cn.edu.fzu.mytoolbox.fragment.ScratchCardFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.random.Random

class ScratchCardView (context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    //初始化绑定类
    private val binding = ViewScratchCardBinding.inflate(LayoutInflater.from(context), this, true)
    // 定义一个int变量来记录剩余的次数
    private var chanceNum=3

    // 设置手指动画效果
    private val handMove = ObjectAnimator.ofPropertyValuesHolder(binding.scratchHand,
        PropertyValuesHolder.ofFloat("translationX",  0f, -30f, 0f),
        PropertyValuesHolder.ofFloat("translationY",  0f, -30f, 0f)
    ).apply {
        duration = 2000 // 设置动画时间为4秒
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = LinearInterpolator()
    }

    init {

        // 设置ViewPager2
        val pagerAdapter= EmbeddedViewPagerAdapter(context as FragmentActivity)
        // 给adapter添加四个fragment对象，可以自定义fragment的内容和布局
        pagerAdapter.addFragment(ScratchCardFragment())
        pagerAdapter.addFragment(Fragment())
        
        binding.scratchViewPager.adapter=pagerAdapter

        // 使用TabLayoutMediator来关联TabLayout和ViewPager2，传入tabLayout, viewPager和一个回调函数
        TabLayoutMediator(binding.scratchTabLayout, binding.scratchViewPager) { tab, position ->
            // 在回调函数中，给每个tab设置标题
            // 定义一个数组，存放四个fragment的标题
            val titles = arrayOf("1", "2")
            tab.text = titles[position]
        }.attach() // 调用attach方法来完成关联

        // 随机产生刮卡抽奖结果
        setScratchResult()

        // “再刮一次”绑定事件
        binding.tvRetry.setOnClickListener{
            if (chanceNum > 0) {
                binding.scratchCard.reset()
                setScratchResult()
                chanceNum-- // 剩余次数减一
                binding.tvChanceNum.text = "累计剩余${chanceNum}次" // 更新显示的文本
            }
            // 如果剩余次数为0，就禁用“再刮一次”按钮
            if (chanceNum == 0) {
                binding.tvRetry.isEnabled = false // 禁用按钮
            }
        }

        // 启动小手动画
        handMove.start()

        // 获取刮刮卡实例，设置接口实现
        binding.scratchCard.setOnScratchListener(object : ScratchCard.OnScratchListener {
            override fun onScratch() {
                // 隐藏小手
                binding.btnScratch.visibility = View.GONE
                binding.scratchHand.visibility = View.GONE
            }
        })

        // 隐藏“刮一刮”按钮和小手
        binding.btnScratch.setOnClickListener {
            // 点击时隐藏ImageView并设置ScratchCard为可刮状态
            binding.btnScratch.visibility = View.GONE
            binding.scratchHand.visibility = View.GONE
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handMove.cancel() // 在View被销毁时,取消动画并释放资源
    }

    // 随机产生刮卡抽奖结果
    fun setScratchResult(){
        if(isWin()){
            binding.scratchCard.setSrcResult(R.drawable.pic_scratch_win)
        }else{
            binding.scratchCard.setSrcResult(R.drawable.pic_scratch_lose)
        }
    }

    //根据随机数的值来判断是否中奖
    fun isWin(): Boolean {
        // 生成一个随机的布尔值
        val bool = Random.nextBoolean()
        // 根据布尔值返回0或1
        return bool
    }
}