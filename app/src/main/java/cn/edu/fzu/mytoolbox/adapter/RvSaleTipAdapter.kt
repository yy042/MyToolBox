package cn.edu.fzu.mytoolbox.adapter

import android.widget.ImageView
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData.FeedListBean.ContentAreaListBean.SaleTipListBean
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvSaleTipAdapter(layoutResId: Int, data: MutableList<SaleTipListBean>) : BaseQuickAdapter<SaleTipListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: SaleTipListBean) {
        when(item.type){
            "1" -> {
                helper.setText(R.id.tvSaleTipTitle, item.title)
                // 隐藏ivSaleTipImage
                helper.setGone(R.id.ivSaleTipImage,true)
            }
            "2" -> {
                // 使用Glide加载图片，传入url和target
                val ivSaleTipImage=helper.getView<ImageView>(R.id.ivSaleTipImage)
                Glide.with(context).load(item.imageUrl).into(helper.getView(R.id.ivSaleTipImage))
                // 隐藏tvSaleTip
                helper.setGone(R.id.tvSaleTipTitle,true)

            }
        }

    }
}



