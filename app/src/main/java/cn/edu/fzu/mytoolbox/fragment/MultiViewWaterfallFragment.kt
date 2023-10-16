package cn.edu.fzu.mytoolbox.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.adapter.FeedWaterfallAdapter
import cn.edu.fzu.mytoolbox.databinding.FragmentMultiViewWaterfallBinding
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.entity.GetFeedListData.FeedListBean
import cn.edu.fzu.mytoolbox.util.setupWaterfall
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MultiViewWaterfallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MultiViewWaterfallFragment : LazyFragment(){

    private var _binding: FragmentMultiViewWaterfallBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // 定义一个回调函数的接口
    interface OnFeedClickListener {
        // 定义一个方法，用于传递feed和position
        fun onFeedClick(feed: FeedListBean, position: Int)
    }

    // 在fragment中创建一个ActivityResultLauncher类型的变量
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    // 在fragment中创建一个ActivityResultLauncher类型的变量
    private lateinit var contactPickerLauncher: ActivityResultLauncher<Intent>
    // 在fragment中创建一个全局变量，用于保存position
    var position: Int = -1

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // 请求使用通讯录权限
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // 如果用户同意了权限请求，执行相应的操作
                    startContactPicker(position)
                } else {
                    // 如果用户拒绝了权限请求，提示用户
                    Toast.makeText(requireContext(), "需要通讯录权限才能继续", Toast.LENGTH_SHORT).show()
                }
            }

        // 处理从通讯录页面选号码后跳转回来的操作
        // 使用ActivityResultContracts.StartActivityForResult契约和一个回调函数来初始化这个变量
        contactPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val contactUri = result.data?.data ?: return@registerForActivityResult
                val cursor = requireContext().contentResolver.query(contactUri, null, null, null, null)
                cursor?.use {
                    if (cursor.moveToNext()){
                        val index = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val number = it.getString(index)
                        // 判断手机号的格式是否为11位
                        if (number.length == 11) {
                            // 如果是正确的格式，将手机号的格式转换为“XXX **** XXX”并显示出来
                            val formattedNumber = number.substring(0, 3) + " **** " + number.substring(7, 11)
                            // 在这里执行后续的操作，例如将号码显示到TextView中
                            val holder = binding.rvFeedWaterfall.findViewHolderForAdapterPosition(position) as BaseViewHolder // 获取对应position的holder
                            holder.getView<TextView>(R.id.tvFeedQuickRechargeNumber).text = formattedNumber // 将号码设置到TextView中
                        } else {
                            // 如果不是正确的格式，提示手机号无效之类的信息
                            Toast.makeText(requireContext(), "手机号无效，请重新选择", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMultiViewWaterfallBinding.inflate(inflater,container,false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun initData() {
        super.initData()
        // 从本地json文件中读取json字符串
        val json = requireActivity().assets.open("feed.json").bufferedReader().use { it.readText() }
        // 使用Gson对象将json字符串转换为TabListBean对象列表
        val gson = Gson()
        val GetFeedListData = gson.fromJson(json, GetFeedListData::class.java)
        val feedList: List<FeedListBean> = GetFeedListData.feedList

        // 创建一个回调函数的实例
        val onFeedClickListener = object : OnFeedClickListener {
            // 重写回调函数的方法
            override fun onFeedClick(feed: FeedListBean, position: Int) {
                // 将position赋值给全局变量
                this@MultiViewWaterfallFragment.position = position
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // 如果有权限，直接跳转到通讯录选择号码界面
                    startContactPicker(position)
                } else {
                    // 如果没有权限，请求权限
                    requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
            }
        }

        // 创建adapter的实例，并将回调函数作为参数传递给adapter的构造函数
        val rvWaterfallAdapter= FeedWaterfallAdapter(mutableListOf(), onFeedClickListener)
        setupWaterfall(
            binding.rvFeedWaterfall, //传入recyclerView对象
            rvWaterfallAdapter,
            feedList
        )

        binding.rvFeedWaterfall.isNestedScrollingEnabled=true


    }

    override fun getContentViewId(): Int {
        return R.layout.fragment_multi_view_waterfall
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MultiViewWaterfallFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MultiViewWaterfallFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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
        // 使用launch方法来启动意图，并等待结果返回
        contactPickerLauncher.launch(intent)
    }

}