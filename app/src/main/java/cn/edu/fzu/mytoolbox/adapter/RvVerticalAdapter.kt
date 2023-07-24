package cn.edu.fzu.mytoolbox.adapter

import android.widget.TextView
import android.widget.Toast
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

        // 获取这个按钮的对象
        val btn = helper.getView<TextView>(R.id.btn_vertical_go)
        // 给这个按钮设置一个onClickListener
        btn.setOnClickListener {
            // 获取当前item的序号
            val position = helper.adapterPosition
            // 执行您想要的操作，例如弹出一个提示框
            Toast.makeText(btn.context, "您点击了第${position + 1}个item的按钮", Toast.LENGTH_SHORT).show()

        }

    }
}