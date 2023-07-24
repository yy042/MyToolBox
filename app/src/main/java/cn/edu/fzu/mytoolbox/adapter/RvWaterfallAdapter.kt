package cn.edu.fzu.mytoolbox.adapter

import android.widget.Toast
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemWaterfall
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvWaterfallAdapter(layoutResId: Int, data: MutableList<ItemWaterfall>) : BaseQuickAdapter<ItemWaterfall, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemWaterfall) {
        helper.setImageResource(R.id.iv_waterfall_pic, item.imageID)
        helper.setText(R.id.tv_waterfall_title, item.title)
        helper.setText(R.id.tv_waterfall_content,item.content)

        if (item.label1!="0"){
            helper.setText(R.id.tv_waterfall_label1,item.label1)
            helper.setGone(R.id.tv_waterfall_label1,false)
        }else{
            helper.setGone(R.id.tv_waterfall_label1,true)
        }

        if (item.label2!="0"){
            helper.setGone(R.id.tv_waterfall_label2,false)
            helper.setText(R.id.tv_waterfall_label2,item.label2)
        }else{
            helper.setGone(R.id.tv_waterfall_label2,true)
        }

        if(item.content!="no"){
            helper.setText(R.id.tv_waterfall_content,item.content)
            helper.setGone(R.id.tv_waterfall_content,false)
        }else{
            helper.setGone(R.id.tv_waterfall_content,true)
            helper.setGone(R.id.tv_waterfall_icon_rmb,true)
            helper.setGone(R.id.tv_waterfall_monthly,true)
        }

        // 给item的view设置一个onClickListener
        helper.itemView.setOnClickListener {
            // 获取当前item的序号
            val position = helper.adapterPosition
            // 弹出一个提示框，内容与item的序号有关
            Toast.makeText(helper.itemView.context, "您点击了第${position + 1}个item", Toast.LENGTH_SHORT).show()
        }


    }
}