package cn.edu.fzu.mytoolbox.adapter

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class FeedWaterfallAdapter(layoutResId: Int, data: MutableList<GetFeedListData.FeedListBean>) : BaseQuickAdapter<GetFeedListData.FeedListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder, item: GetFeedListData.FeedListBean) {
        if(item.picArea == null){
            // 纯文字无图区的item
            helper.setGone(R.id.ivFeedPic1,true)
            helper.setGone(R.id.ivFeedPic2,true)
            helper.setGone(R.id.ivFeedPic3,true)
            helper.setGone(R.id.ivFeedPic4,true)
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

                        helper.setText(R.id.tvFeed, ""+height)
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
            val position = helper.adapterPosition
            helper.setText(R.id.tvFeed, "第${position + 1}个item,图片个数是${item.picArea.picList.size}")
        }

    }
}