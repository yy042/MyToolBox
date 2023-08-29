package cn.edu.fzu.mytoolbox

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.fzu.mytoolbox.adapter.*
import cn.edu.fzu.mytoolbox.databinding.ActivityRechargeSuccessBinding
import cn.edu.fzu.mytoolbox.entity.*
import cn.edu.fzu.mytoolbox.util.ImmersiveToolbar
import cn.edu.fzu.mytoolbox.util.Util.dpToPx
import cn.edu.fzu.mytoolbox.util.Util.setStatusBarTextColor
import cn.edu.fzu.mytoolbox.util.Util.setupRecyclerView
import cn.edu.fzu.mytoolbox.util.Util.setupSpacingRecyclerView
import cn.edu.fzu.mytoolbox.util.Util.setupWaterfall
import cn.edu.fzu.mytoolbox.util.Util.transparentStatusBar
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class RechargeSuccessActivity : AppCompatActivity() {

    private lateinit var rvServiceAdapter: RvServicesAdapter
    private lateinit var rv3ServiceAdapter: RvServicesAdapter
    private lateinit var rvTaskAdapter: RvTasksAdapter
    private lateinit var rvRecommendAdapter: RvRecommendsAdapter
    private lateinit var rvCardAdapter: RvCardsAdapter
    private lateinit var rvWaterfallAdapter: RvWaterfallAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRechargeSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

/*
         //设置标题栏
        ImmersionBar.with(this)
            .statusBarColor(R.color.bg_main)//设置状态栏颜色
            .statusBarDarkFont(true) //设置状态栏字体为深色
            .titleBar(binding.tbMain) //指定标题栏为toolbar
            .init()

            //设置沉浸式状态栏的另一种方法
        //transparentStatusBar(window) //使状态栏背景透明
        //setStatusBarTextColor(window,false)//根据背景色设置状态栏文字颜色
*/

        //设置标题栏
        binding.mainToolbar.setHorizontalPadding(8.5.dpToPx(this), 13.dpToPx(this))
        binding.mainToolbar.setBarColor(R.color.bg_main,true)
        binding.mainToolbar.setTitleText("")
        binding.mainToolbar.setOnBackClickListener(object:ImmersiveToolbar.OnBackClickListener{
            override fun onBackClick() {
                // 创建一个Dialog对象
                val dialog = Dialog(this@RechargeSuccessActivity)
                // 调用setContentView方法，传入弹窗样式XML文件的id
                dialog.setContentView(R.layout.layout_exit_popup)
                // 调用show方法来显示弹窗
                dialog.show()
            }

        })

        //设置RvServices
        rvServiceAdapter= RvServicesAdapter(R.layout.item_service,mutableListOf())
        setupSpacingRecyclerView(
            this,
            binding.rvServices, //传入recyclerView对象
            rvServiceAdapter,
            listOf( //传入数据列表
                ItemService("充流量", R.drawable.ic_mobiledata, "流量告急速订购"),
                ItemService("开通自动充", R.drawable.ic_autorecharge, "专治忘充值"),
                ItemService("电子发票", R.drawable.ic_receipt, "批量开票不排队"),
                ItemService("充值记录", R.drawable.ic_bill, "可查全网记录")
            ),
            LinearLayoutManager.HORIZONTAL
        )

        //设置Rv3Services
        rv3ServiceAdapter= RvServicesAdapter(R.layout.item_service,mutableListOf())
        setupSpacingRecyclerView(
            this,
            binding.rvServices3, //传入recyclerView对象
            rv3ServiceAdapter,
            listOf( //传入数据列表
                ItemService("充流量", R.drawable.ic_mobiledata, "流量告急速订购"),
                ItemService("开通自动充", R.drawable.ic_autorecharge, "专治忘充值"),
                ItemService("电子发票", R.drawable.ic_receipt, "批量开票不排队")
            ),
            LinearLayoutManager.HORIZONTAL
        )

        //设置RvTasks
        rvTaskAdapter= RvTasksAdapter(R.layout.item_task,mutableListOf())
        setupRecyclerView(
            binding.rvTasks, //传入recyclerView对象
            rvTaskAdapter,
            listOf( //传入数据列表
                ItemTask("开启自动充值得金豆", R.drawable.ic_activaterecharge, "首次开启得","0","10金豆","去开启"),
                ItemTask("看科普视频得金豆", R.drawable.ic_video, "15s得多看多得...","0/6","5金豆","去看看")
            ),
            LinearLayoutManager.VERTICAL
        )

        //设置RvRecommends
        rvRecommendAdapter= RvRecommendsAdapter(R.layout.item_recommend,mutableListOf())
        setupRecyclerView(
            binding.rvRecommends, //传入recyclerView对象
            rvRecommendAdapter,
            listOf( //传入数据列表
                ItemRecommend("腾讯视频会员周卡", R.drawable.ic_tencentvideo, "1000金豆"),
                ItemRecommend("优酷视频会员周卡", R.drawable.ic_youku, "1500金豆")
            ),
            LinearLayoutManager.HORIZONTAL
        )


        //设置RvCards
        /*   rvCardAdapter= RvCardsAdapter(R.layout.item_card,mutableListOf())
           setupRecyclerView(
               binding.rvCards, //传入recyclerView对象
               rvCardAdapter,
               listOf( //传入数据列表
                   ItemCard("无门槛", "翻","0","0"),
                   ItemCard("无门槛", "爱奇艺","会员优惠券","去使用"),
                   ItemCard("无门槛", "很遗憾","未抽中奖品","去使用")
               ),
               LinearLayoutManager.HORIZONTAL
           )
   */

        //设置Waterfall
      /*  rvWaterfallAdapter= RvWaterfallAdapter(R.layout.item_waterfall,mutableListOf())
        setupWaterfall(
            binding.rvWaterfall, //传入recyclerView对象
            rvWaterfallAdapter,
            listOf( //传入数据列表
                ItemWaterfall("0", "0","电信关爱版-为老年人架桥","no",R.drawable.pic_elderly),
                ItemWaterfall("赠新人礼包", "赠美团神券","加装【副卡】，一份套餐全家用","10",R.drawable.pic_family),
                ItemWaterfall("免运费", "送配件","iPhone12 128GB 红色 双卡双待","300",R.drawable.pic_iphone),
                ItemWaterfall("赠新人礼包", "0","15GB定向流量+腾讯视频月会员卡","10",R.drawable.pic_tencentvip)
            )
        )*/

        //测试文字自适应
        binding.etTest.addTextChangedListener(object : TextWatcher {
            //在文字改变之前被调用，可以做一些准备工作
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            //在文字改变之中被调用，可以获取输入的文字并显示在TextView上
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //把输入的文字转换为字符串
                val input = s.toString()
                //把字符串设置给TextView
                binding.ictvTest.setText(input)
            }

            //在文字改变之后被调用，可以做一些后续工作
            override fun afterTextChanged(s: Editable?) {
                //do nothing
            }
        })

        //设置任意布局跑马灯View
        val count = 5
        val views: MutableList<View> = ArrayList()
        val inflater = LayoutInflater.from(this)
        for (i in 0 until count) {
            views.add(inflateView(inflater, "加装【副卡】，一份套餐全家用,15GB定向流量+腾讯视频月会员卡", "金豆$i"))
        }
        binding.layoutMarqueeHint.setViewList(views)

        //设置textSwitcher
    /*    // 创建一个数据列表
        val messages = listOf(
            "《赋得古原草送别》《赋得古原草送别》《赋得古原草送别》《赋得古原草送别》《赋得古原草送别》《赋得古原草送别》",
            "离离原上草，一岁一枯荣。",
            "野火烧不尽，春风吹又生。",
            "远芳侵古道，晴翠接荒城。",
            "又送王孙去，萋萋满别情。"
        )
        val marqueeView=binding.marqueeView
        marqueeView.setDataList(messages)
        //二、开始滚动
        marqueeView.startScroll()
        //或者定义刷新的时间
        marqueeView.startScroll(8000)*/
    }

    private fun inflateView(
        inflater: LayoutInflater, //不要使用可空类型
        name: String,
        desc: String
    ): View {
        //不要重新创建inflater对象，直接使用传入的参数
        //不要把marqueeRoot作为父容器传入，而是传入null
        val view = inflater.inflate(R.layout.item_marquee, null, false)
        //检查view是否为null，如果是，则抛出异常或返回默认view
        if (view == null) {
            throw RuntimeException("Failed to inflate view")
            //或者 return TextView(this).apply { text = "Error" }
        }
        val viewName = view.findViewById<TextView>(R.id.marquee_name)
        val viewDesc = view.findViewById<TextView>(R.id.marquee_desc)
        viewName.text = name
        //设置文本字数超出时的水平滚动
        viewName.isSelected = true
        viewName.setSingleLine()
        viewName.ellipsize = TextUtils.TruncateAt.MARQUEE
        viewDesc.text = desc
        return view
    }

    // Override the onActivityResult method
/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check if the request code matches the one we set before
        if (requestCode == 1) {
            // Check if the result code is OK
            if (resultCode == Activity.RESULT_OK) {
                // Get the card result from the data intent
                val cardResult = data?.getBooleanExtra("card_result", false) ?: false
                binding.layoutDraw.updateCardResult(cardResult)
            }
        }
    }*/

}