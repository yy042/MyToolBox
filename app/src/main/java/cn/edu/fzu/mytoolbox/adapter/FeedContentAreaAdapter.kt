package cn.edu.fzu.mytoolbox.adapter

import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.util.StrikeTextView
import cn.edu.fzu.mytoolbox.util.Util
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class FeedContentAreaAdapter(data: MutableList<GetFeedListData.FeedListBean.ContentAreaListBean>) :
    BaseMultiItemQuickAdapter<GetFeedListData.FeedListBean.ContentAreaListBean, BaseViewHolder>(data) {

    // 创建一个HashMap，用于保存每个item的倒计时对象
    private val timers = HashMap<Int, CountDownTimer>()
    private val handler = Handler()

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
                // 图标
                if(item.location.icon.isNullOrBlank())
                    holder.setGone(R.id.ivFeedContentLocationIcon,true)
                else
                    Glide.with(context).load(item.location.icon).into(holder.getView(R.id.ivFeedContentLocationIcon))

                // 标题
                if(!item.location.title.isNullOrBlank()){
                    holder.setText(R.id.tvFeedContentLocationTitle,item.location.title)
                }
            }
            GetFeedListData.CONTENTAREA_TYPE.COUNTDOWN.toInt() -> {
                // 找到倒计时相关的View
                val countdownStatusTextView = holder.getView<TextView>(R.id.tvFeedContentCountDownStatus)
                val countdownTimeTextView = holder.getView<TextView>(R.id.tvFeedContentCountDownTime)
                // 获取item的id
                val itemId = holder.adapterPosition

                // 获取item的开始时间和结束时间
                val startTime = item.countDown.startTime
                val endTime = item.countDown.endTime

                // 判断是否有有效的开始时间和结束时间
                if (startTime.isNullOrEmpty() || endTime.isNullOrEmpty() || startTime > endTime) {
                    // 没有有效的时间，不显示倒计时区域
                    handler.post{
                        // 再通知RecyclerView更新界面
                        notifyItemRemoved(itemId)
                        // 再从数据源中移除该item
                        remove(itemId)
                    }
                    return
                }

                // 将时间字符串转换为Date对象
                val format = SimpleDateFormat("yyyyMMddHHmmss")
                val startDate = format.parse(startTime)
                val endDate = format.parse(endTime)

                // 获取当前时间
                val currentDate = Date()

                // 计算剩余时间
                var remainingTime: Long

                if (currentDate < startDate) {
                    // 当前时间小于开始时间，显示距开始
                    countdownStatusTextView.text = "距开始"
                    countdownStatusTextView.background = context.getDrawable(R.drawable.bg_label_yellow_gradient)
                    holder.setTextColor(R.id.tvFeedContentCountDownTime,context.resources.getColor(R.color.text_yellow))
                    holder.itemView.setBackgroundResource(R.drawable.bg_btn_yellow_round_border)
                    remainingTime = startDate.time - currentDate.time
                } else if (currentDate < endDate) {
                    // 当前时间在开始时间和结束时间之间，显示距结束
                    countdownStatusTextView.text = "距结束"
                    countdownStatusTextView.background = context.getDrawable(R.drawable.bg_label_red_gradient)
                    holder.setTextColor(R.id.tvFeedContentCountDownTime,context.resources.getColor(R.color.text_price_red))
                    holder.itemView.setBackgroundResource(R.drawable.bg_btn_red_round_border)
                    remainingTime = endDate.time - currentDate.time
                } else {
                    // 当前时间大于结束时间，取消倒计时，并移除该item
                    cancelTimer(itemId)
                    // 先将item的高度设置为0，避免RecyclerView重新计算布局
                    holder.itemView.layoutParams.height = 0
                    // RecyclerView正在计算布局或滚动的时候，如果调用notifyDataSetChanged()或者notifyItemRemoved()等方法，会导致RecyclerView的状态不一致从而报错，所以要用handler
                    handler.post{
                        // 再通知RecyclerView更新界面
                        notifyItemRemoved(itemId)
                        // 再从数据源中移除该item
                        remove(itemId)
                    }

                    return
                }

                // 启动倒计时，并更新countdownTimeTextView
                startTimer(itemId, remainingTime, countdownTimeTextView)

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

    private fun startTimer(itemId: Int, remainingTime: Long, textView: TextView) {
        // 创建一个CountDownTimer对象，每秒更新一次textView
        val timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 格式化剩余时间，并显示在textView上
                textView.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                // 倒计时结束，取消timer，并移除该item
                cancelTimer(itemId)
                // 通知RecyclerView更新界面
                notifyItemRemoved(itemId)
                // 移除该item
                remove(itemId)
            }
        }

        // 启动timer
        timer.start()

        // 将timer保存在HashMap中，以便后续取消
        timers[itemId] = timer
    }

    private fun cancelTimer(itemId: Int) {
        // 从HashMap中获取对应的timer对象
        val timer = timers[itemId]

        // 如果存在，就取消timer，并从HashMap中移除
        if (timer != null) {
            timer.cancel()
            timers.remove(itemId)
        }
    }

    private fun formatTime(millis: Long): String {
        // 将毫秒转换为秒、分、时、天
        val seconds = millis / 1000 % 60
        val minutes = millis / (60 * 1000) % 60
        val hours = millis / (60 * 60 * 1000) % 24
        val days = millis / (24 * 60 * 60 * 1000)

        // 如果超过一天，前面显示天数，否则不显示
        val dayString = if (days > 0) "$days"+"天" else ""

        // 使用String.format方法，添加前导零，并拼接成字符串
        return dayString + String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

}