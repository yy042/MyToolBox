package cn.edu.fzu.mytoolbox.adapter

import cn.edu.fzu.mytoolbox.R
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class FeedPicAreaAdapter(layoutResId: Int, data: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        Glide.with(context).load(item).into(holder.getView(R.id.ivFeedPicOne))
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