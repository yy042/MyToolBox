package cn.edu.fzu.mytoolbox.util
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.databinding.ScratchCardBinding

class ScratchCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    // 定义两个成员变量来存储资源ID
    private var srcResultId: Int
    private var srcFrontId: Int
    // 定义存储图片应设置的宽高的变量
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private var mSrcResult: Bitmap
    private var mSrcFront: Bitmap
    private lateinit var mDstBitmap: Bitmap
    private val mPaint: Paint
    private lateinit var mPath: Path
    private var mStartX: Float = 0f
    private var mStartY: Float = 0f

    // 增加一个变量来控制是否可以刮卡，默认为false，只有当点击或者刮动“刮一刮”按钮时才为true
    private var isScratchable = false
    // 刮奖完成的阈值（百分比）
    private val mCompleteThreshold = 0.25f
    // 刮奖完成的标志位
    private var mIsCompleted = false
    // 统计透明像素的个数
    private var transparentCount = 0
    // 定义一个接口，用来回调按钮的隐藏事件
    interface OnScratchListener {
        fun onScratch()
    }
    // 添加一个成员变量，用来保存这个接口的实例
    private var mOnScratchListener: OnScratchListener? = null

    //初始化绑定类
    private val binding = ScratchCardBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint()
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND // 连接处为圆角
        mPaint.strokeCap = Paint.Cap.ROUND // 端点为圆形
        mPaint.isAntiAlias = true // 抗锯齿
        mPaint.strokeWidth = 30f

        // 从XML属性获取drawable资源id
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScratchCard)
        srcResultId = typedArray.getResourceId(R.styleable.ScratchCard_srcResult, R.drawable.pic_scratch_win)
        srcFrontId = typedArray.getResourceId(R.styleable.ScratchCard_srcFront, R.drawable.layer_scratch_front)
        typedArray.recycle()

        //初始化两张图片
        mSrcResult =
            BitmapFactory.decodeResource(resources, srcResultId)
        mSrcFront =
            BitmapFactory.decodeResource(resources, srcFrontId)
        // 遮盖刮刮卡的前景图也要设置
        binding.scratchFront.setImageResource(srcFrontId)
        //必须给view设置与前景图一样的背景，否则无法绘制
        this.setBackgroundResource(srcFrontId)
    }

    // 测量方法，在这里设置View的宽高为前景层图片的宽高，并调用父类方法
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //根据View宽度和原图宽高比计算要绘制的图片尺寸
        val ratio =
            mSrcFront.height.toFloat() / mSrcFront.width.toFloat() //不转换为Float的话ratio会是0
        bitmapWidth = measuredWidth
        bitmapHeight = (bitmapWidth * ratio).toInt()
        // createScaledBitmap()会根据给定的宽度和高度来裁剪原始Bitmap对象。
        // 如果给定的宽度和高度与原始Bitmap对象的宽高比不一致，那么缩放后的Bitmap对象就会被裁剪掉一部分，从而影响缩放效果。
        mSrcResult = Bitmap.createScaledBitmap(
            mSrcResult,
            bitmapWidth,
            bitmapHeight,
            true
        ) // 将图片缩放（实际上是裁剪）到指定大小
        mSrcFront = Bitmap.createScaledBitmap(
            mSrcFront,
            bitmapWidth,
            bitmapHeight,
            true
        ) // 将图片缩放（实际上是裁剪）到指定大小

        mDstBitmap = Bitmap.createBitmap(
            mSrcFront.width,
            mSrcFront.height,
            Bitmap.Config.ARGB_8888
        )
        mPath = Path()

        setMeasuredDimension(bitmapWidth, bitmapHeight)
    }

    override fun onDraw(canvas: Canvas) {
        // 调用父类方法，绘制子视图和背景
        super.onDraw(canvas)

        //画最终呈现的图
        canvas.drawBitmap(mSrcResult, 0f, 0f, mPaint)

        // 只有当可以刮卡时才绘制手指轨迹和前景层，否则不绘制，保持原样
        if (isScratchable) {
            val layerId = canvas.saveLayer(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                null,
                Canvas.ALL_SAVE_FLAG
            )
            //把手指轨迹画到画布上
            val c = Canvas(mDstBitmap)
            c.drawPath(mPath, mPaint)
            //利用SRC_OUT绘制原图
            canvas.drawBitmap(mDstBitmap, 0f, 0f, mPaint)
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
            canvas.drawBitmap(mSrcFront, 0f, 0f, mPaint)
            mPaint.xfermode = null
            canvas.restoreToCount(layerId)
        }

        // 检查刮奖比例是否超出1/4
        checkScratchRatio()

    }

    // 增加一个触摸事件，当用户刮动“开始刮”的图片时，也隐藏ImageView并设置ScratchCard为可刮状态，并调用父类方法
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 调用父类方法，处理子视图的触摸事件
        super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 请求父布局不要拦截事件，让自定义刮卡类处理事件
                // 事件拦截：谁拦截，谁处理
                parent.requestDisallowInterceptTouchEvent(true)
                mStartX = event.x
                mStartY = event.y
                // 调用接口方法，通知父布局隐藏按钮
                mOnScratchListener?.onScratch()
                //首次触碰刮刮卡时，隐藏前景以及设为可刮
                if(binding.scratchFront.visibility == View.VISIBLE) {
                    binding.scratchFront.visibility = View.GONE
                    isScratchable = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // 请求父布局不要拦截事件，让自定义刮卡类处理事件
                // 事件拦截：谁拦截，谁处理
                parent.requestDisallowInterceptTouchEvent(true)
                val endX = event.x
                val endY = event.y
                // 如果可以刮卡，就更新手指轨迹
                if (isScratchable) {
                    mPath.moveTo(mStartX, mStartY)
                    mPath.lineTo(endX, endY)
                    invalidate()
                }
                mStartX = endX
                mStartY = endY
            }
            // 其他类型的动作不做处理
            else -> {
                // 请求父布局可以拦截事件，恢复正常的滑动逻辑
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return true
    }

    private fun checkScratchRatio() {
        // 如果已经完成，则直接返回
        if (mIsCompleted) return

        // 获取覆盖层bitmap的像素数组
        val pixels = IntArray(bitmapWidth * bitmapHeight)
        mSrcFront?.getPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight)

        for (pixel in pixels) {
            if (pixel == Color.TRANSPARENT) {
                transparentCount++
            }
        }

        // 计算刮开的百分比
        val percent = transparentCount.toFloat() / pixels.size

        // 如果百分比超过了阈值，则认为刮奖完成
        if (percent >= mCompleteThreshold) {
            mIsCompleted = true
            isScratchable = false
            invalidate()
        }
    }

    // 设置这个接口的实例的方法
    fun setOnScratchListener(listener: OnScratchListener) {
        mOnScratchListener = listener
    }

    fun setSrcResult(drawableId: Int) {
        // decodeResource()方法会根据当前设备的屏幕密度（density）来缩放图片，可能使它的宽度和高度与原始图片的宽高比不一致
        // 所以此处不应该将bitmap赋值给mSrcResult，而应该使用decodeResource()
        mSrcResult =
            BitmapFactory.decodeResource(resources, drawableId)
        invalidate() // 重新绘制View
    }

    fun setSrcFront(drawableId: Int) {
        mSrcFront =
            BitmapFactory.decodeResource(resources, drawableId)
        invalidate() // 重新绘制View
    }

    fun reset(){
        // 重置标志
        mIsCompleted=false
        isScratchable = true
        // 重新初始化刮刮卡视图的所有属性
        mSrcResult = BitmapFactory.decodeResource(resources, srcResultId)
        mSrcFront = BitmapFactory.decodeResource(resources, srcFrontId)
        // 必须给view设置与前景图一样的背景，否则无法绘制，因为继承自ConstraintLayout的自定义View，会自动为子视图设置背景。
        // 如果不设置背景图片，那么ConstraintLayout会使用默认的背景色。
        this.setBackgroundResource(srcFrontId)
        mDstBitmap = Bitmap.createBitmap(
            mSrcFront.width,
            mSrcFront.height,
            Bitmap.Config.ARGB_8888
        )
        mPath = Path()
        // 重置透明像素个数
        transparentCount = 0

        requestLayout()
        invalidate() // 重新绘制View
    }
}
