package cn.edu.fzu.mytoolbox.adapter

import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemRecommend
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvRecommendsAdapter(layoutResId: Int, data: MutableList<ItemRecommend>) : BaseQuickAdapter<ItemRecommend, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemRecommend) {
        helper.setText(R.id.tv_recommend_title, item.title)
        helper.setImageResource(R.id.iv_recommend_icon, item.imageID)
        helper.setText(R.id.tv_recommend_bean_num,item.content)
        if (item.title == "优酷视频会员周卡") {
            helper.setVisible(R.id.tv_recommend_label,true)
        }

    }
}



