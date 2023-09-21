package cn.edu.fzu.mytoolbox.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.util.MarqueeLayout
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import cn.edu.fzu.mytoolbox.util.Util.setupRecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FeedWaterfallAdapter(data: MutableList<GetFeedListData.FeedListBean>) :
    BaseMultiItemQuickAdapter<GetFeedListData.FeedListBean, BaseViewHolder>(data){

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
            R.layout.item_feed_recharge
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

    override fun convert(helper: BaseViewHolder, item: GetFeedListData.FeedListBean) {
        when (helper.itemViewType) {
            GetFeedListData.FEED_LIST_ITEM_TYPE.LIVE.toInt() -> {
                // 处理直播类型的数据和视图
            }
            GetFeedListData.FEED_LIST_ITEM_TYPE.VIDEO.toInt()  -> {
                // 处理视频类型的数据和视图
            }
            GetFeedListData.FEED_LIST_ITEM_TYPE.ADVERTISE.toInt()  -> {
                // 处理广告类型的数据和视图
                // 获取ViewPager2组件的引用
                val viewPager = helper.getView<ViewPager2>(R.id.feedRollViewPager)
                // 获取TabLayout组件的引用
                val tabLayout = helper.getView<TabLayout>(R.id.feedRollTabLayout)
                // 获取图片url的列表
                val images = item.picArea.picList
                // 创建一个列表，用来存放ImageView对象
                val imageViews = mutableListOf<ImageView>()
                // 遍历图片url列表，为每个url创建一个ImageView对象，并添加到列表中
                for (image in images) {
                    // 创建一个ImageView对象
                    val imageView = ImageView(this@FeedWaterfallAdapter.context)
                    // 设置ImageView的布局参数，宽度为match_parent，高度为wrap_content
                    imageView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    // 设置ImageView的缩放类型为fit_center，保持图片比例居中显示
                    imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    // 使用Glide库加载图片url到ImageView中
                    Glide.with(this@FeedWaterfallAdapter.context).load(image.imageUrl).into(imageView)
                    // 将ImageView对象添加到列表中
                    imageViews.add(imageView)
                }
                // 在列表的首尾分别添加最后一个和第一个ImageView对象，实现无限循环的效果
                imageViews.add(0, imageViews.last())
                imageViews.add(imageViews.first())
                // 设置ViewPager2的适配器，使用lambda表达式创建一个匿名函数
                viewPager.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    // 返回图片的数量
                    override fun getItemCount(): Int {
                        return imageViews.size
                    }

                    // 创建每个页面的视图
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): RecyclerView.ViewHolder {
                        // 创建一个ViewHolder对象，传入对应位置的ImageView对象
                        return object : RecyclerView.ViewHolder(imageViews[viewType]) {}
                    }

                    // 绑定每个页面的视图和数据
                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        // 无需做任何操作，因为视图和数据已经在创建时绑定了
                    }

                    // 返回每个页面的类型，即它在列表中的位置
                    override fun getItemViewType(position: Int): Int {
                        return position
                    }
                }

                // 设置ViewPager2的滚动监听器，实现无限循环轮播的效果
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                    // 当页面被选中时，调用此方法
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // 如果当前页面是第一页，就跳转到倒数第二页（因为最后一页是重复的）
                        if (position == 0) {
                            viewPager.setCurrentItem(imageViews.size - 2, false)
                        }
                        // 如果当前页面是最后一页，就跳转到第二页（因为第一页是重复的）
                        if (position == imageViews.size - 1) {
                            viewPager.setCurrentItem(1, false)
                        }
                    }
                })

                // 使用TabLayoutMediator将TabLayout和ViewPager2绑定起来，实现指示器的功能
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    // 设置指示器文本为空字符串，只显示圆点
                    tab.text = ""
                }.attach()

                // 设置ViewPager2的初始页面为第二页（因为第一页是重复的）
                viewPager.setCurrentItem(1, false)

            }
            GetFeedListData.FEED_LIST_ITEM_TYPE.RECHARGE.toInt()  -> {
                // 处理充值类型的数据和视图
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
}