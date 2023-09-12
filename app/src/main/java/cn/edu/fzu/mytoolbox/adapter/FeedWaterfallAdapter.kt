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
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.util.MarqueeLayout
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import cn.edu.fzu.mytoolbox.util.Util.setupRecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class FeedWaterfallAdapter(layoutResId: Int, data: MutableList<GetFeedListData.FeedListBean>) : BaseQuickAdapter<GetFeedListData.FeedListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: GetFeedListData.FeedListBean) {
        // 主图区的处理
        if(item.picArea == null){
            // 纯文字无图区的item
            helper.setGone(R.id.ivFeedPic1,true)
            helper.setGone(R.id.ivFeedPic2,true)
            helper.setGone(R.id.ivFeedPic3,true)
            helper.setGone(R.id.ivFeedPic4,true)
            helper.setGone(R.id.marqueeFeedCommentList,true)
            helper.setGone(R.id.tvFeedStock,true)

        }else{
            // 根据图片区域的type来显示单图或者多图样式，并设置图片区域底部是否需要圆角（根据内容区域是否为空）
            when(item.picArea.type){
                "1" -> { // 单图
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

                }
                "2" -> { // 多图
                    // 根据图片列表的数量来决定是2图样式还是4图样式，并加载相应的图片到多图视图中
                    when (item.picArea.picList.size){
                        1 -> {
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
                }
                else -> { // 其他情况
                    // 在这里写其他情况的处理逻辑，例如隐藏图片视图、显示错误信息等
                }
            }
           /* val position = helper.adapterPosition
            helper.setText(R.id.rvFeedContentArea, "第${position + 1}个item,图片个数是${item.picArea.picList.size}")*/

            // 处理topImage
            // 处理commentList
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

        }
        
        // 文字区的处理
        if(item.contentAreaList.isNullOrEmpty()){
            helper.setGone(R.id.rvFeedContentArea,true)
        }else{
            val rvContentAreaAdapter= FeedContentAreaAdapter(mutableListOf())
            setupRecyclerView(helper.getView<RecyclerView>(R.id.rvFeedContentArea),rvContentAreaAdapter,item.contentAreaList,LinearLayoutManager.VERTICAL,10.dpToPx(context))
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