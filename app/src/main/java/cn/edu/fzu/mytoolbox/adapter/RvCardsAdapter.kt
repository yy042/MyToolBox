package cn.edu.fzu.mytoolbox.adapter

import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemCard
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvCardsAdapter(layoutResId: Int, data: MutableList<ItemCard>) : BaseQuickAdapter<ItemCard, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemCard) {
        if(item.title=="翻"){
            helper.setBackgroundResource(R.id.layout_card,R.drawable.ic_flipcard)

            helper.setVisible(R.id.tv_card_label,false)
            helper.setVisible(R.id.tv_card_title,false)
            helper.setVisible(R.id.tv_card_content,false)
            helper.setVisible(R.id.btn_card_go,false)

        }else{
            //判断item.label是否为"0"
            if (item.title != "很遗憾") {
                //如果不为"0"，则设置tv_card_label为可见，并设置文本
                helper.setVisible(R.id.tv_card_label, true)
            }else {
                //如果为"0"，则设置tv_card_label为不可见
                helper.setVisible(R.id.tv_card_label, false)
            }

            helper.setText(R.id.tv_card_title, item.title)
            helper.setText(R.id.tv_card_content,item.content)

            if (item.title == "很遗憾"){
                helper.setVisible(R.id.btn_card_go,false)
            }

        }

    }
}