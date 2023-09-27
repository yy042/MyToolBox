package cn.edu.fzu.mytoolbox

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import cn.edu.fzu.mytoolbox.adapter.*
import cn.edu.fzu.mytoolbox.databinding.ActivityRechargeSuccessBinding
import cn.edu.fzu.mytoolbox.entity.*
import cn.edu.fzu.mytoolbox.entity.GetFeedListData.FeedListBean
import cn.edu.fzu.mytoolbox.util.FeedView
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

class RechargeSuccessActivity : AppCompatActivity(), FeedView.OnFeedClickListener {

    private lateinit var rvServiceAdapter: RvServicesAdapter
    private lateinit var rv3ServiceAdapter: RvServicesAdapter
    private lateinit var rvTaskAdapter: RvTasksAdapter
    private lateinit var rvRecommendAdapter: RvRecommendsAdapter
    private lateinit var rvCardAdapter: RvCardsAdapter
    private lateinit var rvWaterfallAdapter: RvWaterfallAdapter

    private lateinit var viewPager: ViewPager2

    // 定义一个回调函数的接口
    interface OnContactIconClickListener {
        // 定义一个方法，用于传递点击事件和数据
        fun onContactIconClick(view: View, data: FeedListBean)
    }


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

        viewPager=binding.layoutFeed.findViewById(R.id.feedViewPager)

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

    override fun onFeedClick(feed: FeedListBean, position: Int) {
        // 处理点击事件，判断是否有通讯录权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // 如果有权限，直接跳转到通讯录选择号码界面
            startContactPicker(position)
        } else {
            // 如果没有权限，请求权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE_PICK_CONTACT)
        }
    }

    // 跳转到通讯录选择号码界面的函数
    fun startContactPicker(position: Int) {
        // 创建一个隐式意图，指定动作为选择联系人
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        // 设置结果类型为电话号码
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        // 传递position作为额外数据
        intent.putExtra("position", position)
        // 启动意图，并等待结果返回
        startActivityForResult(intent, REQUEST_CODE_PICK_CONTACT)
    }

    // 请求权限的回调函数
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PICK_CONTACT -> {
                // 如果用户同意了权限请求，跳转到通讯录选择号码界面
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContactPicker(-1) // 传递-1表示没有指定position
                } else {
                    // 如果用户拒绝了权限请求，提示用户需要权限才能继续
                    Toast.makeText(this, "您需要授予通讯录权限才能选择号码", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 接收返回结果的函数
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_PICK_CONTACT -> {
                // 如果结果是成功的，并且有数据返回
                if (resultCode == Activity.RESULT_OK && data != null) {
                    // 获取返回的数据的URI
                    val contactUri = data.data ?: return
                    // 查询URI对应的联系人的电话号码
                    val cursor = this.contentResolver.query(contactUri, null, null, null, null)
                    cursor?.use {
                        // 如果有结果，取出第一条记录的电话号码字段
                        // 使用getcolumnindexorthrow方法来替代getcolumnindex方法
                        // 这个方法在找不到指定列名时会抛出一个异常，而不是返回-1
                        val index = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val number = it.getString(index)
                        // 将电话号码显示在tv1中
                        Log.d("Contact", "Phone number is $number")
                    }
                }
            }
        }
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

    companion object {
        const val REQUEST_CODE_PICK_CONTACT = 1
    }


}