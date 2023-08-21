package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.databinding.IconTextviewBinding
import kotlinx.coroutines.*

//自定义View类
class IconTextView(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs), CoroutineScope by MainScope() {

    //初始化绑定类
    private val binding = IconTextviewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        //设置自定义View的方向为水平
        orientation = HORIZONTAL

        //获取自定义属性的值
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconTextView)
        val icLoadingSrc = typedArray.getResourceId(
            R.styleable.IconTextView_icLoadingSrc,
            R.id.ic_loading
        ) // 获取图片资源的值，如果没有指定，默认为0
        val icLoadingSize = typedArray.getDimensionPixelSize(
            R.styleable.IconTextView_icLoadingSize,
            dpToPx(16)
        ) // 获取图片尺寸的值，如果没有指定，默认为16dp
        val text = typedArray.getString(R.styleable.IconTextView_text) // 获取文字内容的值，如果没有指定，默认为null
        val textColor = typedArray.getColor(
            R.styleable.IconTextView_textColor,
            Color.parseColor("#fefefe")
        ) // 获取文字颜色的值，如果没有指定，默认为黑色
        typedArray.recycle() // 回收TypedArray对象

        //根据自定义属性的值来设置ImageView和TextView
        if (icLoadingSrc != 0) {
            binding.icLoading.setImageResource(icLoadingSrc) // 如果图片资源不为0，设置图片资源
            binding.icLoading.layoutParams.width = icLoadingSize // 设置图片宽度
            binding.icLoading.layoutParams.height = icLoadingSize // 设置图片高度
            binding.icLoading.visibility = View.GONE //隐藏loading图片

        }
        if (text != null) {
            setText(text) // 如果文字内容不为null，设置文字内容
        }
        binding.tvCheck.setTextColor(textColor) // 设置文字颜色

        //创建旋转动画
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 1000
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.interpolator = LinearInterpolator()
        //设置点击事件
        setOnClickListener {
            //启动或停止旋转动画
            if (binding.icLoading.animation == null) {
                binding.icLoading.visibility = View.VISIBLE //显示loading图片
                setText("查询中...") //更新文字内容
                binding.icReload.visibility = View.INVISIBLE //隐藏重新加载图片
                binding.icLoading.startAnimation(rotateAnimation)
                //启动一个协程来模拟网络请求
                launch(Dispatchers.Main) {
                    delay(3000L) //延迟3秒
                    binding.icLoading.clearAnimation() //停止旋转动画
                    binding.icLoading.visibility = View.GONE //隐藏loading图片
                    setText("余额：58.00元")  //更新文字内容
                    binding.icReload.visibility = View.VISIBLE //显示重新加载图片
                }
            } else {
                binding.icLoading.clearAnimation()
                setText("查询余额")  //更新文字内容
                binding.icLoading.visibility = View.VISIBLE //显示loading图片
                binding.icReload.visibility = View.INVISIBLE //隐藏重新加载图片
            }
        }
    }

    fun setText(text: String) {
        binding.tvCheck.text = text
    }

    //将dp转换为px的辅助方法
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
