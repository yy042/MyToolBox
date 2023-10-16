package cn.edu.fzu.mytoolbox.adapter

import android.widget.ImageView
import cn.edu.fzu.mytoolbox.R
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class FeedPicAreaAdapter(layoutResId: Int,
                         data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        // 在图片加载之前添加一些延迟，以确保视图已被正确初始化
        // 为了解决第1个fragment个别item图片来不及加载的情况：其他fragment在切换到它们时，可能有更多的时间用于图片加载，因此它们的图片可以正常显示。
        val view = holder.getView<ImageView>(R.id.ivFeedPicOne)
        view.postDelayed({
            Glide.with(context).load(item).into(view)
        }, 200)
    }

    override fun getItemCount(): Int {
        return if (data.size < 4) {
            // 如果 `data` 中包含的项少于 4 个，则取前 2 个展示
            Math.min(data.size, 2)
        } else {
            // 如果 `data` 中包含的项大于等于 4 个，则取前 4 个展示
            Math.min(data.size, 4)
        }
    }
}