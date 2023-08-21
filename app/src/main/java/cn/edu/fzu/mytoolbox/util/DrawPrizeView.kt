package cn.edu.fzu.mytoolbox.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import cn.edu.fzu.mytoolbox.CardActivity
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.databinding.ViewDrawPrizeBinding

class DrawPrizeView (context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs){
    //添加一个布尔型的属性，用来判断是否关闭了动画程序时长缩放的适配
    private var isAnimationScaleOff = false
    //初始化绑定类
    private val binding = ViewDrawPrizeBinding.inflate(LayoutInflater.from(context), this, true)

    //创建一个ActivityResultLauncher类型的属性，用来存储注册的回调函数
    private val resultLauncher = (context as ActivityResultCaller).registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object :
        ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult?) {
            //根据result.resultCode和result.data来判断抽奖结果，并调用updateCardResult方法来更新UI
            if (result?.resultCode == Activity.RESULT_OK) {
                val cardResult = result.data?.getIntExtra("card_result", 0) ?: 0
                updateCardResult(cardResult)

            }
        }
    })

    init {
        orientation=VERTICAL

        //获取当前的动画程序时长缩放的值
        val animationScale = Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        //如果该值为0，则表示关闭了动画程序时长缩放的适配
        isAnimationScaleOff = animationScale == 0f
        Log.i("TAG","isAnimationScaleOff is $isAnimationScaleOff")

        binding.mainCardResult.setOnClickListener{
            // 创建一个Intent对象，指定要跳转到的Activity类
            val intent = Intent(context, CardActivity::class.java)

            //判断是否关闭了动画程序时长缩放的适配
            if (isAnimationScaleOff) {
                // 如果是，直接启动CardActivity，并注册回调函数
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)// 禁用启动Activity时的动画
                resultLauncher.launch(intent)

            }else{
                // 创建一个 ActivityOptionsCompat 对象
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity, // 需要将context强制转换为Activity类型
                    binding.mainCardResult,
                    ViewCompat.getTransitionName(binding.mainCardResult) ?: "card" // 使用ViewCompat类来获取视图的transitionName，如果为空则使用默认值
                )

                /*val p1 = Pair<View, String>(binding.mainCardResult, ViewCompat.getTransitionName(binding.mainCardResult) ?: "card")
                val p2 = Pair<View, String>(binding.tvCongrats, ViewCompat.getTransitionName(binding.tvCongrats) ?: "text")

                //调用makeSceneTransitionAnimation方法时，使用*运算符来展开Pair对象的数组
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, *arrayOf(p1, p2))*/

                // 使用ActivityResultLauncher对象的launch方法来启动Intent对象，并传入options.toBundle()作为参数
                resultLauncher.launch(intent, options)
            }

        }
    }

    fun updateCardResult(result:Int){
        // 根据抽卡结果更新UI
        if (result==2){
            binding.mainCardResult.setImageResource(R.drawable.ic_flip_card)
        }
        else if (result==0) {
            // Show the card win layout in the custom view
            binding.mainCardResult.setImageResource(R.drawable.ic_flip_card_win)
        } else if (result==1){
            // Show the card lose layout in the custom view
            binding.mainCardResult.setImageResource(R.drawable.ic_flip_card_lose)
        }

        invalidate()
    }

}