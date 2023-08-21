package cn.edu.fzu.mytoolbox.util

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import cn.edu.fzu.mytoolbox.R

// 定义一个ScratchCard类，继承自View
class ScratchCardDefault(context: Context, attrs: AttributeSet) :
    View(context, attrs) {

    private var bitmapWidth = 0
    private var bitmapHeight = 0

    private lateinit var mSrcResult: Bitmap
    private lateinit var mSrcBitmap: Bitmap
    private lateinit var mDstBitmap: Bitmap
    private val mPaint: Paint
    private lateinit var mPath: Path
    private var mStartX: Float = 0f
    private var mStartY: Float = 0f

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint()
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND // 连接处为圆角
        mPaint.strokeCap = Paint.Cap.ROUND // 端点为圆形
        mPaint.isAntiAlias = true // 抗锯齿
        mPaint.strokeWidth = 30f
    }

    // 测量方法，在这里设置View的宽高为前景层图片的宽高
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mSrcResult = BitmapFactory.decodeResource(resources, R.drawable.pic_scratch_win)
        mSrcBitmap = BitmapFactory.decodeResource(resources, R.drawable.layer_scratch_front)

        //根据View宽度和原图宽高比计算要绘制的图片尺寸
        val ratio = mSrcBitmap.height.toFloat() / mSrcBitmap.width.toFloat() //不转换为Float的话ratio会是0
        bitmapWidth = measuredWidth
        bitmapHeight = (bitmapWidth * ratio).toInt()
        mSrcResult = Bitmap.createScaledBitmap(mSrcResult!!, bitmapWidth, bitmapHeight, true) // 将图片缩放（实际上是裁剪）到指定大小
        mSrcBitmap = Bitmap.createScaledBitmap(mSrcBitmap!!, bitmapWidth, bitmapHeight, true) // 将图片缩放（实际上是裁剪）到指定大小

        mDstBitmap = Bitmap.createBitmap(mSrcBitmap.width, mSrcBitmap.height, Bitmap.Config.ARGB_8888)
        mPath = Path()

        setMeasuredDimension(bitmapWidth, bitmapHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画最终呈现的图
        canvas.drawBitmap(mSrcResult, 0f, 0f, mPaint)
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        //把手指轨迹画到画布上
        val c = Canvas(mDstBitmap)
        c.drawPath(mPath, mPaint)
        //利用SRC_OUT绘制原图
        canvas.drawBitmap(mDstBitmap, 0f, 0f, mPaint)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        canvas.drawBitmap(mSrcBitmap, 0f, 0f, mPaint)
        mPaint.xfermode = null
        canvas.restoreToCount(layerId)
    }

    // 在自定义 View 的 onTouchEvent() 方法中重写的，该方法会在 View 被触摸时被调用，参数 event 是一个 MotionEvent 对象，表示触摸事件的信息。
    // 在此方法中也顺便进行了滑动冲突的处理。
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 使用 when 表达式来判断触摸事件的动作类型
        when (event.action) {
            // 如果是按下动作
            MotionEvent.ACTION_DOWN -> {
                // 请求父布局不要拦截事件，让自定义刮卡类处理事件
                // 事件拦截：谁拦截，谁处理
                parent.requestDisallowInterceptTouchEvent(true)
                // 将 Path 对象移动到按下的坐标点
                mPath.moveTo(event.x, event.y)
                // 记录按下的坐标点作为起始点
                mStartX = event.x
                mStartY = event.y
                // 返回 true 表示消费了这个事件
                return true
            }
            // 如果是移动动作
            MotionEvent.ACTION_MOVE -> {
                // 请求父布局不要拦截事件，让自定义刮卡类处理事件
                parent.requestDisallowInterceptTouchEvent(true)
                // 计算起始点和当前点的中点作为终点
                val endX = (mStartX + event.x) / 2
                val endY = (mStartY + event.y) / 2
                // 使用 quadTo() 方法在 Path 对象上添加一段二次贝塞尔曲线，控制点为起始点，终点为中点
                mPath.quadTo(mStartX, mStartY, endX, endY)
                // 更新起始点为当前点
                mStartX = event.x
                mStartY = event.y
            }
            // 其他类型的动作不做处理
            else -> {
                // 请求父布局可以拦截事件，恢复正常的滑动逻辑
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        // 调用 postInvalidate() 方法使 View 无效，触发 onDraw() 方法重新绘制 View
        postInvalidate()
        // 调用父类的 onTouchEvent() 方法处理其他逻辑
        return super.onTouchEvent(event)
    }

}