package cn.edu.fzu.mytoolbox.adapter

import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemService
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvServicesAdapter(layoutResId: Int, data: MutableList<ItemService>) : BaseQuickAdapter<ItemService, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemService) {
        helper.setText(R.id.tv_service_title, item.title)
        helper.setImageResource(R.id.iv_service_icon, item.imageID)
        helper.setText(R.id.tv_service_content,item.content)

    }
}



