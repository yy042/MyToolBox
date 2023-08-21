package cn.edu.fzu.mytoolbox.adapter

import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.ItemTask
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvTasksAdapter(layoutResId: Int, data: MutableList<ItemTask>) : BaseQuickAdapter<ItemTask, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: ItemTask) {
        helper.setText(R.id.tv_task_title, item.title)
        helper.setImageResource(R.id.iv_task_icon, item.imageID)
        helper.setText(R.id.tv_task_content,item.content)
        //判断item.taskLabel是否为"0"
        if (item.taskLabel != "0") {
            //如果不为"0"，则设置tv_task_label为可见，并设置文本
            helper.setVisible(R.id.tv_task_label, true)
            helper.setText(R.id.tv_task_label,item.taskLabel)
        } else {
            //如果为"0"，则设置tv_task_label为不可见
            helper.setVisible(R.id.tv_task_label, false)
        }
        helper.setText(R.id.tv_task_bean_num,item.beanNum)
        helper.setText(R.id.btn_task_go,item.buttonText)

    }
}