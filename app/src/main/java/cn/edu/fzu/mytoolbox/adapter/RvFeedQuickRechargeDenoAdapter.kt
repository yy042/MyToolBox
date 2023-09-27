package cn.edu.fzu.mytoolbox.adapter

import android.widget.TextView
import android.widget.Toast
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData.FeedListBean.QuickRechargeBean.DenominationBean
import cn.edu.fzu.mytoolbox.entity.ItemVertical
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RvFeedQuickRechargeDenoAdapter(layoutResId: Int, data: MutableList<DenominationBean>) : BaseQuickAdapter<DenominationBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: DenominationBean) {

        helper.setText(R.id.tvFeedQuickRechargeDenoMainTitle,item.mainTitle)
        helper.setText(R.id.tvFeedQuickRechargeDenoSubTitle,item.subtitle)

    }
}