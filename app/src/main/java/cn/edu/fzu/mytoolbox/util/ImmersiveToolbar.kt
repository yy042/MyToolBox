package cn.edu.fzu.mytoolbox.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import androidx.appcompat.widget.Toolbar
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.databinding.ImmersiveToolbarBinding
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import com.gyf.immersionbar.ImmersionBar

// 定义一个自定义标题栏View类，继承自Toolbar
class ImmersiveToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    // 定义一个返回按钮的点击事件监听器接口
    interface OnBackClickListener {
        fun onBackClick()
    }

    // 定义一个返回按钮的点击事件监听器变量
    private var onBackClickListener: OnBackClickListener? = null

    // 定义一个标题文本的变量
    private var titleText: String? = null
    // 定义一个状态栏颜色的变量
    private var statusBarColor: Int = R.color.white
    // 定义一个状态栏字体颜色的变量
    private var statusBarDarkFont: Boolean = true

    private var verticalPadding: Int = 0
    private var horizontalPadding: Int = 0

    private var iconWidth: Double = 0.0

    // 定义一个ViewBinding的变量
    private var binding: ImmersiveToolbarBinding

    // 初始化代码块，在创建对象时执行
    init {
        // 从布局文件中加载视图，并绑定到ViewBinding变量中
        binding = ImmersiveToolbarBinding.inflate(LayoutInflater.from(context), this, true)
        // 从属性集中获取自定义属性的值，如果有的话
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImmersiveToolbar)
        titleText = typedArray.getString(R.styleable.ImmersiveToolbar_titleText)
        statusBarColor = typedArray.getResourceId(R.styleable.ImmersiveToolbar_statusBarColor, R.color.white)
        statusBarDarkFont = typedArray.getBoolean(R.styleable.ImmersiveToolbar_statusBarDarkFont, true)
        verticalPadding = typedArray.getDimensionPixelSize(R.styleable.ImmersiveToolbar_verticalPadding,10)
        horizontalPadding = typedArray.getDimensionPixelSize(R.styleable.ImmersiveToolbar_horizontalPadding,10)
        iconWidth = typedArray.getDimensionPixelSize(R.styleable.ImmersiveToolbar_iconBackWidth,10).toDouble()

        typedArray.recycle()

        // 设置标题文本视图的文本为自定义属性的值，如果有的话
        binding.tvTitle?.text = titleText ?: "标题"
        // 设置返回按钮的点击事件监听器，调用接口中的方法
        binding.ivBack?.setOnClickListener {
            onBackClickListener?.onBackClick()
            // 获取返回按钮的父视图
            val parent = binding.ivBack.parent as View
            // 在父视图布局完成后执行
            parent.post {
                //扩大返回按钮的点击区域
                // 创建一个矩形对象，用来存储返回按钮的边界
                val rect = Rect()
                // 获取返回按钮的边界，并赋值给矩形对象
                binding.ivBack.getHitRect(rect)
                val enlarge=30.dpToPx(context)
                // 扩大矩形对象的边界，分别增加20dp
                rect.top -= enlarge
                rect.left -= enlarge
                rect.bottom += enlarge
                rect.right += enlarge
                // 创建一个TouchDelegate对象，传入矩形对象和返回按钮对象
                val touchDelegate = TouchDelegate(rect, binding.ivBack)
                // 把TouchDelegate对象设置给父视图
                parent.touchDelegate = touchDelegate
            }
        }

        // 使用ImmersionBar库处理沉浸式，设置状态栏和标题栏颜色一致，以及其他参数
        setBarColor(statusBarColor,statusBarDarkFont)

        //设置padding
        setPadding(iconWidth,horizontalPadding,verticalPadding)

    }

    fun setBarColor(color: Int=R.color.white,isDarkFont:Boolean=true){
        val colorValue = getResources().getColor(color) // 转换颜色资源id为颜色值
        binding.root.setBackgroundColor(colorValue)
        //处理沉浸式
        ImmersionBar.with(context as Activity)
            .statusBarColor(color) // 状态栏颜色为自定义属性的值，如果有的话
            .fitsSystemWindows(true) // 适配刘海屏或者水滴屏等异形屏幕
            .statusBarDarkFont(isDarkFont) // 状态栏字体颜色为自定义属性的值，如果有的话
            .titleBar(this)
            .init()
    }

    // 定义一个设置返回按钮点击事件监听器的方法，供外部调用
    fun setOnBackClickListener(listener: OnBackClickListener) {
        onBackClickListener = listener
    }

    // 定义一个设置标题文本的方法，供外部调用
    fun setTitleText(text: String) {
        titleText = text
        binding.tvTitle?.text = titleText
    }

    // 定义一个设置水平方向上的padding的方法，供外部调用
    fun setHorizontalPadding(icWidth: Double, padding: Int) {
        horizontalPadding = padding
        iconWidth=icWidth

        binding.ivBack.layoutParams.width = (iconWidth + padding * 2).toInt()
        binding.ivBack.setPadding(horizontalPadding,verticalPadding,horizontalPadding,verticalPadding)

        binding.tvRight.setPadding(horizontalPadding,verticalPadding,horizontalPadding,verticalPadding)

        requestLayout() // 请求重新布局
    }

    // 定义一个获取水平方向上的padding的方法，供外部调用
    fun getHorizontalPadding(): Int {
        return horizontalPadding
    }

    // 定义一个设置垂直方向上的padding的方法，供外部调用
    fun setVerticalPadding(padding: Int) {
        verticalPadding = padding
        binding.ivBack.setPaddingRelative(horizontalPadding,verticalPadding,horizontalPadding,verticalPadding)
        binding.tvRight.setPadding(horizontalPadding,verticalPadding,horizontalPadding,verticalPadding)
        requestLayout() // 请求重新布局
    }

    // 定义一个获取垂直方向上的padding的方法，供外部调用
    fun getVerticalPadding(): Int {
        return verticalPadding
    }

    // 统一设置横纵padding
    fun setPadding(icWidth:Double, horiPadding:Int, vertiPadding:Int){
        iconWidth = icWidth
        horizontalPadding = horiPadding
        verticalPadding = vertiPadding

        binding.ivBack.layoutParams.width = (iconWidth + horiPadding * 2).toInt()
        binding.ivBack.setPadding(horizontalPadding,verticalPadding,horizontalPadding,verticalPadding)

        binding.tvRight.setPadding(horizontalPadding,verticalPadding,horizontalPadding,verticalPadding)
        requestLayout() // 请求重新布局
    }

}

