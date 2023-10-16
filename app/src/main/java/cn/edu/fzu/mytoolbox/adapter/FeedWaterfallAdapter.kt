package cn.edu.fzu.mytoolbox.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.entity.GetFeedListData.FeedListBean
import cn.edu.fzu.mytoolbox.fragment.MultiViewWaterfallFragment
import cn.edu.fzu.mytoolbox.util.MarqueeLayout
import cn.edu.fzu.mytoolbox.util.dpToPx
import cn.edu.fzu.mytoolbox.util.setupGridRecyclerView
import cn.edu.fzu.mytoolbox.util.setupRecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class FeedWaterfallAdapter(data: MutableList<FeedListBean>,
                           val listener: MultiViewWaterfallFragment.OnFeedClickListener,
                           val lifecycleOwner: LifecycleOwner
) :
    BaseMultiItemQuickAdapter<FeedListBean, BaseViewHolder>(data){

    init {
        // 根据type值，添加不同的布局文件
        addItemType(
            GetFeedListData.FEED_LIST_ITEM_TYPE.LIVE.toInt(),
            R.layout.item_feed_pic_area_one_image
        )
        addItemType(
            GetFeedListData.FEED_LIST_ITEM_TYPE.VIDEO.toInt(),
            R.layout.item_feed_pic_area_one_image
        )
        addItemType(
            GetFeedListData.FEED_LIST_ITEM_TYPE.ADVERTISE.toInt(),
            R.layout.item_feed_advertise
        )
         addItemType(
            GetFeedListData.FEED_LIST_ITEM_TYPE.RECHARGE.toInt(),
            R.layout.item_feed_quick_recharge
        )
        addItemType(
            GetFeedListData.FEED_ADAPTER_ITEM_TYPE.ONE_IMAGE,
            R.layout.item_feed_card_common
        )
        addItemType(
            GetFeedListData.FEED_ADAPTER_ITEM_TYPE.MANY_IMAGE,
            R.layout.item_feed_card_common
        )
        addItemType(
            GetFeedListData.FEED_ADAPTER_ITEM_TYPE.NULL,
            R.layout.item_feed_card_common
        )

    }

    override fun convert(helper: BaseViewHolder, item: FeedListBean) {
        when (helper.itemViewType) {
            GetFeedListData.FEED_LIST_ITEM_TYPE.LIVE.toInt() -> {
                // 处理直播类型的数据和视图
            }
            GetFeedListData.FEED_LIST_ITEM_TYPE.VIDEO.toInt()  -> {
                // 处理视频类型的数据和视图
            }
            GetFeedListData.FEED_LIST_ITEM_TYPE.ADVERTISE.toInt()  -> {
                // 处理广告类型的数据和视图
                val banner=helper.getView<Banner<String, BannerImageAdapter<String>>>(R.id.feedAdvertiseScroller)
                val adList=item.adLists
                var picList= mutableListOf<String>()
                for(ad in adList){
                    picList.add(ad.imageUrl)
                }
                banner.apply{
                    addBannerLifecycleObserver(lifecycleOwner)
                    setIndicator(CircleIndicator(context))
                    scrollTime = 500
                    setAdapter(object :BannerImageAdapter<String>(picList){
                        override fun onBindView(
                            holder: BannerImageHolder,
                            data: String?,
                            position: Int,
                            size: Int
                        ) {
                            Glide.with(context).load(data).into(holder.imageView)
                        }

                    })
                }

                recyclerView.post{
                    val itemWidth=helper.itemView.width
                    val itemHeight=itemWidth * 212 / 157
                    // 设置这个item的高度为根据宽高比算出的值
                    helper.itemView.layoutParams.height=itemHeight
                    // 设置banner的高度和item一致
                    banner.layoutParams.height=itemHeight

                }

            }
            GetFeedListData.FEED_LIST_ITEM_TYPE.RECHARGE.toInt()  -> {
                // 处理充值类型的数据和视图
                helper.setText(R.id.tvFeedQuickRechargeTitle,item.quickRecharge.title)
                val rvRechargeAdapter= RvFeedQuickRechargeDenoAdapter(R.layout.item_feed_quick_recharge_denomination,mutableListOf())
                setupRecyclerView(helper.getView<RecyclerView>(R.id.rvFeedQuickRechargeDenomination),
                    rvRechargeAdapter,
                    item.quickRecharge.denominations,
                    LinearLayoutManager.VERTICAL,
                    10.dpToPx(context))

                // 为ivFeedQuickRechargeContactIcon设置点击监听器
                helper.getView<ImageView>(R.id.ivFeedQuickRechargeContactIcon).setOnClickListener {
                    // 调用listener的onFeedClick方法，传递feed和position
                    listener.onFeedClick(item, helper.adapterPosition)
                }

            }
            GetFeedListData.FEED_ADAPTER_ITEM_TYPE.ONE_IMAGE -> {
                // 处理单图类型的数据和视图
                // 单图
                val rvGridAdapter = FeedPicAreaAdapter(R.layout.item_feed_pic_area_one_image, mutableListOf())
                val urlList: List<String> = listOf(item.picArea.imageUrl)
                setupGridRecyclerView(
                    helper.getView(R.id.rvFeedPicArea), //传入recyclerView对象
                    rvGridAdapter,
                    urlList,
                    1,
                    0f,
                    RecyclerView.VERTICAL
                )

                // 处理topImage
                if(item.picArea.topImage.isNullOrBlank()){
                    helper.setGone(R.id.ivFeedTopImage,true)
                }else{
                    // 使用Glide加载网络图片到ivFeedTopImage中
                    val ivTopImage=helper.getView<ImageView>(R.id.ivFeedTopImage)
                    Glide.with(context).load(item.picArea.topImage).into(ivTopImage)
                }

                // 处理commentList
                if(item.picArea.commentList == null){
                    helper.setGone(R.id.marqueeFeedCommentList,true)
                }else{
                    val commentList=item.picArea.commentList
                    //设置任意布局跑马灯View
                    val count = item.picArea.commentList.size
                    val views: MutableList<View> = ArrayList()
                    val inflater = LayoutInflater.from(context)
                    for (i in 0 until count) {
                        views.add(inflateView(inflater, commentList[i].title,commentList[i].fontColor,commentList[i].backgroundColor))
                    }
                    helper.getView<MarqueeLayout>(R.id.marqueeFeedCommentList).setViewList(views)
                }

                // 处理stock
                if(item.picArea.stock.isNullOrBlank()){
                    helper.setGone(R.id.tvFeedStock,true)
                }else{
                    // 设置显示的库存状态
                    helper.setText(R.id.tvFeedStock,item.picArea.stock)
                }
                // 文字区的处理
                if(item.contentAreaList.isNullOrEmpty()){
                    helper.setGone(R.id.rvFeedContentArea,true)
                }else{
                    val rvContentAreaAdapter= FeedContentAreaAdapter(mutableListOf())
                    setupRecyclerView(helper.getView<RecyclerView>(R.id.rvFeedContentArea),rvContentAreaAdapter,item.contentAreaList,LinearLayoutManager.VERTICAL,10.dpToPx(context))
                }
            }
            GetFeedListData.FEED_ADAPTER_ITEM_TYPE.MANY_IMAGE -> {
                // 处理多图类型的数据和视图
                // 主图区的处理
                // 根据图片列表的数量来决定是2图样式还是4图样式，并加载相应的图片到多图视图中
                val rvGridAdapter = FeedPicAreaAdapter(R.layout.item_feed_pic_area_one_image, mutableListOf())
                val urlList: List<String> = item.picArea.picList.map { it.imageUrl }
                val itemCount = if (urlList.size < 4) {
                    2
                } else {
                    4
                }
                setupGridRecyclerView(
                    helper.getView(R.id.rvFeedPicArea), //传入recyclerView对象
                    rvGridAdapter,
                    urlList.subList(0, itemCount),
                    2,
                    0f,
                    RecyclerView.VERTICAL
                )
                /* val position = helper.adapterPosition
                 helper.setText(R.id.rvFeedContentArea, "第${position + 1}个item,图片个数是${item.picArea.picList.size}")*/

                // 处理topImage
                if(item.picArea.topImage.isNullOrBlank()){
                    helper.setGone(R.id.ivFeedTopImage,true)
                }else{
                    // 使用Glide加载网络图片到ivFeedTopImage中
                    val ivTopImage=helper.getView<ImageView>(R.id.ivFeedTopImage)
                    Glide.with(context).load(item.picArea.topImage).into(ivTopImage)
                }

                // 处理commentList
                if(item.picArea.commentList == null){
                    helper.setGone(R.id.marqueeFeedCommentList,true)
                }else{
                    val commentList=item.picArea.commentList
                    //设置任意布局跑马灯View
                    val count = item.picArea.commentList.size
                    val views: MutableList<View> = ArrayList()
                    val inflater = LayoutInflater.from(context)
                    for (i in 0 until count) {
                        views.add(inflateView(inflater, commentList[i].title,commentList[i].fontColor,commentList[i].backgroundColor))
                    }
                    helper.getView<MarqueeLayout>(R.id.marqueeFeedCommentList).setViewList(views)
                }

                // 处理stock
                if(item.picArea.stock.isNullOrBlank()){
                    helper.setGone(R.id.tvFeedStock,true)
                }else{
                    // 设置显示的库存状态
                    helper.setText(R.id.tvFeedStock,item.picArea.stock)
                }

                // 文字区的处理
                if(item.contentAreaList.isNullOrEmpty()){
                    helper.setGone(R.id.rvFeedContentArea,true)
                }else{
                    val rvContentAreaAdapter= FeedContentAreaAdapter(mutableListOf())
                    setupRecyclerView(helper.getView<RecyclerView>(R.id.rvFeedContentArea),rvContentAreaAdapter,item.contentAreaList,LinearLayoutManager.VERTICAL,10.dpToPx(context))
                }

            }
            GetFeedListData.FEED_ADAPTER_ITEM_TYPE.NULL -> {
                // 处理无主图区类型的数据和视图
                // 隐藏库存标签
                helper.setGone(R.id.tvFeedStock,true)
                // 文字区的处理
                if(item.contentAreaList.isNullOrEmpty()){
                    helper.setGone(R.id.rvFeedContentArea,true)
                }else{
                    val rvContentAreaAdapter= FeedContentAreaAdapter(mutableListOf())
                    setupRecyclerView(helper.getView<RecyclerView>(R.id.rvFeedContentArea),rvContentAreaAdapter,item.contentAreaList,LinearLayoutManager.VERTICAL,10.dpToPx(context))
                }
            }

        }

    }

    private fun inflateView(
        inflater: LayoutInflater, //不要使用可空类型
        title: String,
        fontColor: String, // 字体颜色
        backgroundColor: String // 背景颜色
    ): View {
        //不要重新创建inflater对象，直接使用传入的参数
        //不要把marqueeRoot作为父容器传入，而是传入null
        val view = inflater.inflate(R.layout.item_feed_comments_marquee, null, false)
        //检查view是否为null，如果是，则抛出异常或返回默认view
        if (view == null) {
            throw RuntimeException("Failed to inflate view")
            //或者 return TextView(this).apply { text = "Error" }
        }
        val viewTitle = view.findViewById<TextView>(R.id.tvFeedComment)
        viewTitle.text = title
        //设置文本字数超出时的水平滚动
        viewTitle.isSelected = true
        viewTitle.setSingleLine()
        viewTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        // 使用viewTitle.setTextColor方法来设置字体颜色
        if (backgroundColor.isNotBlank()){
            viewTitle.setTextColor(Color.parseColor(fontColor))
        }else viewTitle.setTextColor(Color.WHITE)

        // 创建默认的黑色圆角样式
        val defaultDrawable = GradientDrawable()
        defaultDrawable.setColor(context.resources.getColor((R.color.blue_half_transparent)))
        defaultDrawable.cornerRadius = 8.5.dpToPx(context).toFloat()

        // 使用drawable.setColor方法来设置背景的颜色
        if (backgroundColor.isNotBlank())
            defaultDrawable.setColor(Color.parseColor(backgroundColor))
        // 使用viewTitle.setBackground方法来设置背景的Drawable对象
        viewTitle.setBackground(defaultDrawable)
        return view
    }

    companion object {
        private const val REQUEST_PHONE_STATE_PERMISSION = 1001
    }

}