package cn.edu.fzu.mytoolbox.adapter

import android.widget.Toast
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemHorizontal
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvHorizontalAdapter(layoutResId: Int, data: MutableList<ItemHorizontal>) : BaseQuickAdapter<ItemHorizontal, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemHorizontal) {
        helper.setText(R.id.tv_horizontal_title, item.title)
        helper.setImageResource(R.id.iv_horizontal_icon, item.imageID)
        helper.setText(R.id.tv_horizontal_content,item.content)
        // 给item的view设置一个onClickListener
        helper.itemView.setOnClickListener {
            // 获取当前item的序号
            val position = helper.adapterPosition
            // 弹出一个提示框，内容与item的序号有关
            Toast.makeText(helper.itemView.context, "您点击了第${position + 1}个item", Toast.LENGTH_SHORT).show()
        }
    }
}



