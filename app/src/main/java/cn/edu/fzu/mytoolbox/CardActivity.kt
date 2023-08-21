package cn.edu.fzu.mytoolbox

import android.animation.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.transition.addListener
import cn.edu.fzu.mytoolbox.databinding.ActivityCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.gyf.immersionbar.ImmersionBar
import kotlin.random.Random

class CardActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCardBinding
    private lateinit var animationSet:AnimatorSet
    var isAnimationScaleOff=false
    var isWinResult=2 //默认是2，代表无结果
    // 创建一个变量，用来存储卡片是否已经翻开
    var isCardFlipped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //设置沉浸式
        ImmersionBar.with(this)
            .init()

        //获取当前的动画程序时长缩放的值
        val animationScale = Settings.Global.getFloat(this.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        //如果该值为0，则表示关闭了动画程序时长缩放的适配
        isAnimationScaleOff = animationScale == 0f
        Log.i("TAG","isAnimationScaleOff is $isAnimationScaleOff")

/*        // 为tvTest设置自定义的转场动画类TextSizeTransition
        TransitionSet().apply {
            addTransition(TextSizeTransition())
            addTarget(binding.tvTest)
            window.sharedElementEnterTransition = this
            window.sharedElementReturnTransition = this
        }

        val transition: Transition = TransitionSet()
            .addTransition(ChangeTransform()).addTarget(TextView::class.java) // Only for TextViews
            .addTransition(ChangeImageTransform())
            .addTarget(ImageView::class.java) // Only for ImageViews
            .addTransition(ChangeBounds()) // For both

        window.setSharedElementEnterTransition(transition)*/


        // 获取卡片视图和光束视图的引用
        val card = binding.card
        val ray = binding.cardRay
        lateinit var popup:View
        popup=binding.cardLose
        isWinResult=isWin()
        Log.i("TAG","isWinResult is $isWinResult")
        if (isWinResult==0){
            popup=binding.cardWin
        }else{
            popup=binding.cardLose
        }

        val cheer=binding.ivCheer
        val btn_close=binding.btnClose

        // 创建一个动画集合，用于存放所有的动画
        animationSet = AnimatorSet()

        // 创建第二步的动画
        // 卡片旋转由0度转向+15度（旋转时间0.2s）
        val rotateCard1 = ObjectAnimator.ofFloat(card, "rotation", 0f, 15f).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 光束出场从0%缩放至60%（缩放时间0.2s）
        val scaleRay1 = ObjectAnimator.ofPropertyValuesHolder(ray,
            PropertyValuesHolder.ofFloat("scaleX", 0f, 0.6f),
            PropertyValuesHolder.ofFloat("scaleY", 0f, 0.6f)
        ).apply {
            duration = 200  // 设置动画时间为0.2秒
        }


        // 创建第三步的动画
        // 卡片旋转由+15度转向0度（旋转时间0.2s）
        val rotateCard2 = ObjectAnimator.ofFloat(card, "rotation", 15f, 0f).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 光束从60%缩放至50%（缩放时间0.2s）
        val scaleRay2 = ObjectAnimator.ofPropertyValuesHolder(ray,
            PropertyValuesHolder.ofFloat("scaleX", 0.6f, 0.5f),
            PropertyValuesHolder.ofFloat("scaleY", 0.6f, 0.5f)
        ).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 创建第四步的动画
        // 卡片旋转由0度转向-15度（旋转时间0.2s）
        val rotateCard3 = ObjectAnimator.ofFloat(card, "rotation", 0f, -15f).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 光束从50%缩放至60%（缩放时间0.2s）
        val scaleRay3 = ObjectAnimator.ofPropertyValuesHolder(ray,
            PropertyValuesHolder.ofFloat("scaleX", 0.5f, 0.6f),
            PropertyValuesHolder.ofFloat("scaleY", 0.5f, 0.6f)
        ).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 创建第五步的动画
        // 卡片旋转由-15度转向0度（旋转时间0.2s）
        val rotateCard4 = ObjectAnimator.ofFloat(card, "rotation", -15f, 0f).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 光束从60%缩放至50%（缩放时间0.2s）
        val scaleRay4 = ObjectAnimator.ofPropertyValuesHolder(ray,
            PropertyValuesHolder.ofFloat("scaleX", 0.6f, 0.5f),
            PropertyValuesHolder.ofFloat("scaleY", 0.6f, 0.5f)
        ).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 创建第六步的动画
        // 光束从50%缩放至60%（缩放时间0.2s）
        val scaleRay5 = ObjectAnimator.ofPropertyValuesHolder(ray,
            PropertyValuesHolder.ofFloat("scaleX", 0.5f, 0.6f),
            PropertyValuesHolder.ofFloat("scaleY", 0.5f, 0.6f)
        ).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 创建第七步的动画
        // 卡片绕y轴旋转由0度旋转至360度（旋转时间0.4s）
        // 卡片缩放由150%放大至300%（缩放时间0.4s）
        val rotateCard5 = ObjectAnimator.ofPropertyValuesHolder(card,
            PropertyValuesHolder.ofFloat("rotationY", 0f, -270f),
            PropertyValuesHolder.ofFloat("scaleX", 1f, 2f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 2f)
        ).apply {
            duration = 400  // 设置动画时间为0.2秒
            val distance=10000
            val scale=getResources().getDisplayMetrics().density * distance
            card.cameraDistance=scale
        }

        // 光束消失透明度由100%变至0%（变化时间0.2s）
        val fadeRay1 = ObjectAnimator.ofFloat(ray, "alpha", 1f, 0f).apply {
            duration = 200  // 设置动画时间为0.2秒
        }

        // 弹窗绕y轴翻转由270度转至360度（旋转时间0.2s）
        val rotatePopup1 = ObjectAnimator.ofFloat(popup, "rotationY", -270f, -360f).apply {
            duration = 200 // 设置动画时间为0.2秒
            val distance=10000
            val scale=getResources().getDisplayMetrics().density * distance

            // 添加一个UpdateListener
            addUpdateListener {
                // 在每次动画更新时都重新设置distance
                popup.setHasTransientState(true)
                popup.cameraDistance=scale
                popup.invalidate()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    // 动画开始时
                    popup.visibility = View.VISIBLE
                    popup.transitionName="card"
                    // 动画开始时，设置isCardFlipped为true
                    isCardFlipped = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    // 动画结束时
                    // 配置
                    val options = RequestOptions().skipMemoryCache(true)
                    // 加载gif图片
                    // 根据drawable id获取Drawable对象
                    Glide.with(getApplicationContext()).asGif()
                        .apply(options) // 应用配置
                        .load(R.drawable.gif_cheer)
                        .fitCenter() // 使图片宽度适应父布局宽度，且保持宽高比不变
                        .listener(object : RequestListener<GifDrawable> { // 添加监听，设置播放次数
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<GifDrawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: GifDrawable?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<GifDrawable>?,
                                dataSource: com.bumptech.glide.load.DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                if (resource is GifDrawable) {
                                    resource.setLoopCount(1) // 只播放一次
                                }
                                return false
                            }
                        })
                        .into(cheer)
                    cheer.visibility=View.VISIBLE

                    // 显示关闭按钮
                    btn_close.visibility=View.VISIBLE
                    btn_close.setOnClickListener{
                        backToMainActivity()
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                    // 动画取消时，不做任何操作
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    // 动画重复时，不做任何操作
                }
            })
        }

        // 光束出现透明度由0%变至100%（变化时间0.2s）
        val fadeRay2 = ObjectAnimator.ofFloat(ray, "alpha", 0f, 1f).apply {
            duration = 200 // 设置动画时间为0.2秒
        }

        // 光束从60%缩放至100%（缩放时间0.2s）
        val scaleRay6 = ObjectAnimator.ofPropertyValuesHolder(ray,
            PropertyValuesHolder.ofFloat("scaleX", 0.6f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 0.6f, 1f)
        ).apply {
            duration = 200 // 设置动画时间为0.2秒
        }

        // 创建第九步的动画
        // 光束以4s一圈的速度进行旋转
        val rotateRay1 = ObjectAnimator.ofFloat(ray, "rotation", 0f, 360f).apply {
            duration = 4000 // 设置动画时间为4秒
            repeatCount = ValueAnimator.INFINITE // 设置重复次数为无限
            interpolator = LinearInterpolator()
        }

        // 将所有的动画添加到动画集合中，并设置播放顺序
        animationSet.play(rotateCard1).with(scaleRay1)  // 第二步同时播放
        animationSet.play(rotateCard2).with(scaleRay2).after(rotateCard1)  // 第三步在第二步之后播放
        animationSet.play(rotateCard3).with(scaleRay3).after(rotateCard2)  // 第四步在第三步之后播放
        animationSet.play(rotateCard4).with(scaleRay4).after(rotateCard3)  // 第五步在第四步之后播放
        animationSet.play(scaleRay5).after(rotateCard4)  // 第六步在第五步之后播放
        animationSet.play(rotateCard5).with(fadeRay1).after(scaleRay5)  // 第七步在第六步之后播放
        animationSet.play(rotatePopup1).with(fadeRay2).with(scaleRay6).after(rotateCard5) // 第七步中的弹窗翻转、光束出现和光束缩放在卡片旋转之后同时播放
        animationSet.play(rotateRay1).after(rotatePopup1) // 第九步中的光束旋转在弹窗翻转之后播放

        if(!isAnimationScaleOff){
            //动画缩放未关闭
            window.sharedElementEnterTransition.addListener(onEnd = { animationSet.start() })
        }else{
            //动画缩放已关闭
            card.visibility=View.INVISIBLE
            popup.visibility=View.VISIBLE
            // 显示关闭按钮
            btn_close.visibility=View.VISIBLE
            btn_close.setOnClickListener{
                backToMainActivity()
            }
        }

        binding.btnCardUse.setOnClickListener {
            val intent = Intent(this, DrawActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0 , 0)
        }

    }

    override fun onBackPressed() {
        binding.card.rotation=0f
        animationSet.cancel()
        backToMainActivity()

        super.onBackPressed() //这句应该放在onBackPressed()的最后，因为它会直接结束当前的Activity，而不会执行后面的代码。
    }

    fun backToMainActivity(){
        // Create an Intent object to hold the data
        val data = Intent()
        if(isAnimationScaleOff){
            isCardFlipped=true
        }
        if (isCardFlipped) {
            // Put the card result as an extra in the data intent
            data.putExtra("card_result", isWinResult) // isWin() is a boolean value that indicates whether the card is win or lose


        } else {
            // 如果卡片没有翻开，那么显示未翻开的图片
            data.putExtra("card_result", 2) // isWin() is a boolean value that indicates whether the card is win or lose
        }
        // Set a result code for this intent
        val resultCode = Activity.RESULT_OK // You can use Activity.RESULT_CANCELED if the operation is canceled
        // Set the result with the data intent and the result code
        setResult(resultCode, data)
        // 调用supportFinishAfterTransition()方法来结束当前Activity，并启动共享元素转场
        supportFinishAfterTransition()

    }

    //根据随机数的值来判断是否中奖
    fun isWin(): Int {
        // 生成一个随机的布尔值
        val bool = Random.nextBoolean()
        // 根据布尔值返回0或1
        return if (bool) 0 else 1
    }

    // 扩展函数
    fun View.scaleTo(ratio: Float): ObjectAnimator {
        // Create an ObjectAnimator object with property values holder
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            this, // The view object to be animated
            PropertyValuesHolder.ofFloat("scaleX", 1f, ratio), // The scaleX property to be changed
            PropertyValuesHolder.ofFloat("scaleY", 1f, ratio) // The scaleY property to be changed
        )
        // Return the animator object
        return animator
    }


}
