package cn.edu.fzu.mytoolbox

// ScreenUtils.kt
import android.content.Context
import android.util.TypedValue

object ScreenUtils {

    // 将dp值转换为px值
    fun dp2px(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    // 将px值转换为dp值
    fun px2dp(px: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.resources.displayMetrics).toInt()
    }
}
