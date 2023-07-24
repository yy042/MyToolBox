package cn.edu.fzu.mytoolbox.adapter

import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemVertical
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvVerticalAdapter(layoutResId: Int, data: MutableList<ItemVertical>) : BaseQuickAdapter<ItemVertical, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemVertical) {
        helper.setText(R.id.tv_vertical_title, item.title)
        helper.setImageResource(R.id.iv_vertical_icon, item.imageID)
        helper.setText(R.id.tv_vertical_content,item.content)
        helper.setText(R.id.tv_vertical_bean_num,item.beanNum)
        helper.setText(R.id.btn_vertical_go,item.buttonText)

    }
}