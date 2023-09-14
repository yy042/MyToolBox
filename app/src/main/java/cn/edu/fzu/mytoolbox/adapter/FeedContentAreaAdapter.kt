package cn.edu.fzu.mytoolbox.adapter

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.util.StrikeTextView
import cn.edu.fzu.mytoolbox.util.Util
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
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
            R.layout.rv_feed_content_sale_tip
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
                // 设置SaleTip列表
                val rvSaleTipAdapter= RvSaleTipAdapter(R.layout.item_feed_content_sale_tip,mutableListOf())
                Util.setupRecyclerView(
                    holder.getView<RecyclerView>(R.id.rvFeedContentSaleTip),
                    rvSaleTipAdapter,
                    item.saleTipList,
                    LinearLayoutManager.HORIZONTAL,
                    5.dpToPx(context)
                )
            }
            GetFeedListData.CONTENTAREA_TYPE.PRICE.toInt() -> {
                // 售价是否显示人民币符号，不为1则不显示
                if(!item.price.isShowPriceUnit.equals("1")){
                    holder.setGone(R.id.tvFeedContentPriceUnit,true)
                }

                // 售价整数部分
                if(item.price.priceInteger.isNullOrBlank()){
                    holder.setGone(R.id.tvFeedContentPriceInteger,true)
                }else{
                    holder.setText(R.id.tvFeedContentPriceInteger,item.price.priceInteger)
                }

                // 售价小数部分和其他
                if(item.price.priceDecimal.isNullOrBlank()){
                    holder.setGone(R.id.tvFeedContentPriceDecimal,true)
                }else{
                    holder.setText(R.id.tvFeedContentPriceDecimal,item.price.priceDecimal)
                }

                // 售价颜色
                if(!item.price.priceColor.isNullOrBlank()){
                    holder.setTextColor(R.id.tvFeedContentPriceInteger,Color.parseColor(item.price.priceColor))
                }

                // 原价和原价颜色
                if(item.price.originalPrice.isNullOrBlank())
                    holder.setGone(R.id.tvFeedContentOriginalPrice,true)
                else{
                    holder.setText(R.id.tvFeedContentOriginalPrice,item.price.originalPrice)
                    // 设置颜色
                    if(!item.price.priceColor.isNullOrBlank())
                        holder.setTextColor(R.id.tvFeedContentOriginalPrice,Color.parseColor(item.price.originalPriceColor))
                }

                // 原价是否划横线，不为1则不显示
                if(!item.price.isOriginalPriceLine.equals("1"))
                    holder.getView<StrikeTextView>(R.id.tvFeedContentOriginalPrice).strike=false

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