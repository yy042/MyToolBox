package cn.edu.fzu.mytoolbox.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.bgabanner.BGABanner
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.RechargeSuccessActivity
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.entity.GetFeedListData.FeedListBean
import cn.edu.fzu.mytoolbox.util.FeedView
import cn.edu.fzu.mytoolbox.util.MarqueeLayout
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import cn.edu.fzu.mytoolbox.util.Util.setupRecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class FeedWaterfallAdapter(data: MutableList<FeedListBean>,
                           val listener: FeedView.OnFeedClickListener
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
                val banner=helper.getView<BGABanner>(R.id.feedAdvertiseScroller)
                val adList=item.adLists
                var picList= mutableListOf<String>()
                for(ad in adList){
                    picList.add(ad.imageUrl)
                }
                // 设置轮播图的数据源，传入 picList，不需要传入提示文字列表
                banner.setData(picList, null)
                   // 设置轮播图的适配器，用于加载图片，这里使用Glide库作为示例
                banner.setAdapter(BGABanner.Adapter<ImageView, String> { banner, itemView, model, position ->
                    Glide.with(context).load(model).fitCenter().into(itemView)
                })
                // 设置滑动监听器
                banner.setDelegate(object : BGABanner.Delegate<ImageView?, String?> {
                    override fun onBannerItemClick(banner: BGABanner?, itemView: ImageView?, model: String?, position: Int) {
                        // 处理点击事件
                    }

                    fun onBannerPageChanged(position: Int) {
                        // 处理页面切换事件
                    }
                })
                //设置是否允许用户手动滑动，默认是true
                banner.setAllowUserScrollable(true)

                helper.itemView.isNestedScrollingEnabled=true

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
                helper.setVisible(R.id.ivFeedPic1,true)
                helper.setGone(R.id.ivFeedPic2,true)
                helper.setGone(R.id.ivFeedPic3,true)
                helper.setGone(R.id.ivFeedPic4,true)

                // 加载单图图片到单图视图中
                Glide.with(context).load(item.picArea.imageUrl).into(helper.getView(R.id.ivFeedPic1))

                // 根据图片宽高比计算单图视图的高度，并设置给LayoutParams
                recyclerView.post{
                    val width = helper.getView<ImageView>(R.id.ivFeedPic1).width // 获取单图视图的宽度（锁宽）
                    val height = (width / item.picArea.imageRatio.toDouble()).toInt() // 根据宽高比计算高度（等比缩放）
                    val params = helper.getView<ImageView>(R.id.ivFeedPic1).layoutParams as ConstraintLayout.LayoutParams // 获取单图视图的LayoutParams
                    params.height = height // 设置高度

                    //helper.setText(R.id.rvFeedContentArea, ""+height)
                }

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
                when (item.picArea.picList.size){
                    1 -> {
                        // 单图
                        helper.setVisible(R.id.ivFeedPic1,true)
                        helper.setGone(R.id.ivFeedPic2,true)
                        helper.setGone(R.id.ivFeedPic3,true)
                        helper.setGone(R.id.ivFeedPic4,true)

                        // 加载单图图片到单图视图中
                        Glide.with(context).load(item.picArea.imageUrl).fitCenter().into(helper.getView(R.id.ivFeedPic1))

                        // 根据图片宽高比计算单图视图的高度，并设置给LayoutParams
                        recyclerView.post{
                            val width = helper.getView<ImageView>(R.id.ivFeedPic1).width // 获取单图视图的宽度（锁宽）
                            val height = (width / item.picArea.imageRatio.toDouble()).toInt() // 根据宽高比计算高度（等比缩放）
                            val params = helper.getView<ImageView>(R.id.ivFeedPic1).layoutParams as ConstraintLayout.LayoutParams // 获取单图视图的LayoutParams
                            params.height = height // 设置高度
                        }

                    }
                    in 2..3 -> { // 2图样式
                        helper.setVisible(R.id.ivFeedPic1, true) // 显示第一个图片视图
                        helper.setVisible(R.id.ivFeedPic2, true) // 显示第二个图片视图
                        helper.setGone(R.id.ivFeedPic3,true)
                        helper.setGone(R.id.ivFeedPic4,true)

                        // 加载图片列表中的前两个图片到对应的图片视图中
                        Glide.with(context).load(item.picArea.picList[0].imageUrl).into(helper.getView(R.id.ivFeedPic1))
                        Glide.with(context).load(item.picArea.picList[1].imageUrl).into(helper.getView(R.id.ivFeedPic2))
                    }
                    else -> { // 4图样式
                        helper.setVisible(R.id.ivFeedPic1, true) // 显示第一个图片视图
                        helper.setVisible(R.id.ivFeedPic2, true) // 显示第二个图片视图
                        helper.setVisible(R.id.ivFeedPic3, true) // 显示第三个图片视图
                        helper.setVisible(R.id.ivFeedPic4, true) // 显示第四个图片视图

                        // 加载图片列表中的前四个图片到对应的图片视图中
                        Glide.with(context).load(item.picArea.picList[0].imageUrl).into(helper.getView(R.id.ivFeedPic1))
                        Glide.with(context).load(item.picArea.picList[1].imageUrl).into(helper.getView(R.id.ivFeedPic2))
                        Glide.with(context).load(item.picArea.picList[2].imageUrl).into(helper.getView(R.id.ivFeedPic3))
                        Glide.with(context).load(item.picArea.picList[3].imageUrl).into(helper.getView(R.id.ivFeedPic4))
                    }

                }
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
                // 纯文字无图区的item
                helper.setGone(R.id.ivFeedPic1,true)
                helper.setGone(R.id.ivFeedPic2,true)
                helper.setGone(R.id.ivFeedPic3,true)
                helper.setGone(R.id.ivFeedPic4,true)
                helper.setGone(R.id.marqueeFeedCommentList,true)
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