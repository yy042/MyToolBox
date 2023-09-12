package cn.edu.fzu.mytoolbox.adapter

import android.graphics.Color
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class FeedContentAreaAdapter(data: MutableList<GetFeedListData.FeedListBean.ContentAreaListBean>) :
    BaseMultiItemQuickAdapter<GetFeedListData.FeedListBean.ContentAreaListBean, BaseViewHolder>(data) {

    init {
        // 根据type值，添加不同的布局文件
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.MAIN_TITLE.toInt(),
            R.layout.item_feed_content_main_title
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.SALE_TIP.toInt(),
            R.layout.item_feed_content_sale_tip
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.PRICE.toInt(),
            R.layout.item_feed_content_price
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.LOCATION.toInt(),
            R.layout.item_feed_content_location
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.COUNTDOWN.toInt(),
            R.layout.item_feed_content_count_down
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.NUM.toInt(),
            R.layout.item_feed_content_num
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.PIC_ONE.toInt(),
            R.layout.item_feed_content_pic_one
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.PIC_TWO.toInt(),
            R.layout.item_feed_content_pic_two
        )
        addItemType(
            GetFeedListData.CONTENTAREA_TYPE.COMPLETION.toInt(),
            R.layout.item_feed_content_completion
        )
        // 可以根据需要添加更多类型和布局文件
    }

    override fun convert(
        holder: BaseViewHolder,
        item: GetFeedListData.FeedListBean.ContentAreaListBean
    ) {
        // 根据不同的item类型，绑定不同的视图数据
        when (holder.itemViewType) {
            GetFeedListData.CONTENTAREA_TYPE.MAIN_TITLE.toInt() -> {
                // 设置主标题文本
                holder.setText(R.id.tvFeedContentMainTitle, item.mainTitle.title)
                // 设置主标题颜色
                if(item.mainTitle.color.isNotBlank()){
                    holder.setTextColor(
                        R.id.tvFeedContentMainTitle,
                        Color.parseColor(item.mainTitle.color)
                    )
                }

            }
            GetFeedListData.CONTENTAREA_TYPE.SALE_TIP.toInt() -> {

            }
            GetFeedListData.CONTENTAREA_TYPE.PRICE.toInt() -> {

            }
            GetFeedListData.CONTENTAREA_TYPE.LOCATION.toInt() -> {

            }
            GetFeedListData.CONTENTAREA_TYPE.COUNTDOWN.toInt() -> {

            }
            GetFeedListData.CONTENTAREA_TYPE.NUM.toInt() -> {

            }
            GetFeedListData.CONTENTAREA_TYPE.PIC_ONE.toInt() -> {

            }
            GetFeedListData.CONTENTAREA_TYPE.PIC_TWO.toInt() -> {

            }
            GetFeedListData.CONTENTAREA_TYPE.COMPLETION.toInt() -> {

            }
        }

    }
}