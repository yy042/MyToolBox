package cn.edu.fzu.mytoolbox.adapter

import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemHorizontal
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvHorizontalAdapter(layoutResId: Int, data: MutableList<ItemHorizontal>) : BaseQuickAdapter<ItemHorizontal, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemHorizontal) {
        helper.setText(R.id.tv_horizontal_title, item.title)
        helper.setImageResource(R.id.iv_horizontal_icon, item.imageID)
        helper.setText(R.id.tv_horizontal_content,item.content)

    }
}



