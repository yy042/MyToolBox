package cn.edu.fzu.mytoolbox.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.Util.setupRecyclerView
import cn.edu.fzu.mytoolbox.adapter.RvHorizontalAdapter
import cn.edu.fzu.mytoolbox.adapter.RvVerticalAdapter
import cn.edu.fzu.mytoolbox.databinding.FragmentRecyclerViewBinding
import cn.edu.fzu.mytoolbox.entity.ItemHorizontal
import cn.edu.fzu.mytoolbox.entity.ItemVertical
import cn.edu.fzu.mytoolbox.viewPager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecyclerViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecyclerViewFragment() : Fragment() {

    private var _binding: FragmentRecyclerViewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentRecyclerViewBinding.inflate(inflater,container,false)
        //水平RecyclerView
        var rvHorizontalAdapter = RvHorizontalAdapter(R.layout.item_horizontal, mutableListOf())
        setupRecyclerView(
            binding.rvHorizontal, //传入recyclerView对象
            rvHorizontalAdapter,
            listOf( //传入数据列表
                ItemHorizontal("充流量", R.drawable.ic_mobiledata, "流量告急速订购"),
                ItemHorizontal("开通自动充", R.drawable.ic_autorecharge, "专治忘充值"),
                ItemHorizontal("电子发票", R.drawable.ic_receipt, "批量开票不排队"),
                ItemHorizontal("充值记录", R.drawable.ic_bill, "可查全网记录"),
                ItemHorizontal("充流量", R.drawable.ic_mobiledata, "流量告急速订购"),
                ItemHorizontal("开通自动充", R.drawable.ic_autorecharge, "专治忘充值"),
                ItemHorizontal("电子发票", R.drawable.ic_receipt, "批量开票不排队"),
                ItemHorizontal("充值记录", R.drawable.ic_bill, "可查全网记录")
            ),
            LinearLayoutManager.HORIZONTAL
        )
        //设置滑动响应控制
        viewPager?.let { controllScroll(binding.rvHorizontal, it) }

        //垂直RecyclerView
        var rvVerticalAdapter=RvVerticalAdapter(R.layout.item_vertical, mutableListOf())
        setupRecyclerView(
            binding.rvVertical, //传入recyclerView对象
            rvVerticalAdapter,
            listOf( //传入数据列表
                ItemVertical("开启自动充值得金豆", R.drawable.ic_activaterecharge, "首次开启得","10金豆","去开启"),
                ItemVertical("看科普视频得金豆", R.drawable.ic_video, "15s得多看多得...","5金豆","去看看"),
                ItemVertical("开启自动充值得金豆", R.drawable.ic_activaterecharge, "首次开启得","10金豆","去开启"),
                ItemVertical("看科普视频得金豆", R.drawable.ic_video, "15s得多看多得...","5金豆","去看看"),
                ItemVertical("开启自动充值得金豆", R.drawable.ic_activaterecharge, "首次开启得","10金豆","去开启"),
                ItemVertical("看科普视频得金豆", R.drawable.ic_video, "15s得多看多得...","5金豆","去看看"),
                ItemVertical("开启自动充值得金豆", R.drawable.ic_activaterecharge, "首次开启得","10金豆","去开启"),
                ItemVertical("看科普视频得金豆", R.drawable.ic_video, "15s得多看多得...","5金豆","去看看"),

            ),
            LinearLayoutManager.VERTICAL
        )

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecyclerViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    fun controllScroll(recyclerView: RecyclerView, viewPager: ViewPager2){
        // 创建一个RecyclerView.OnItemTouchListener对象
        val itemTouchListener = object : RecyclerView.OnItemTouchListener {
            // 定义一个变量，用来记录上一次的X坐标
            var lastX = 0

            // 重写onInterceptTouchEvent方法，拦截触摸事件
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                // 根据触摸事件的类型，执行不同的操作
                when (e.action) {
                    // 如果是按下事件，记录当前的X坐标
                    MotionEvent.ACTION_DOWN -> {
                        lastX = e.x.toInt()
                    }
                    // 如果是移动事件，判断滑动的方向和位置
                    MotionEvent.ACTION_MOVE -> {
                        // 判断是否是向右滑动
                        val isScrollingRight = e.x > lastX
                        // 如果是向右滑动，并且RecyclerView已经滑动到最左边，那么让ViewPager2处理滑动事件
                        if (isScrollingRight && (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                            viewPager.isUserInputEnabled = true
                        }
                        // 如果是向左滑动，并且RecyclerView已经滑动到最右边，那么让ViewPager2处理滑动事件
                        else if (!isScrollingRight && (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1) {
                            viewPager.isUserInputEnabled = true
                        }
                        // 否则，让RecyclerView处理滑动事件
                        else {
                            viewPager.isUserInputEnabled = false
                        }
                    }
                    // 如果是抬起事件，重置X坐标，并让ViewPager2处理滑动事件
                    MotionEvent.ACTION_UP -> {
                        lastX = 0
                        viewPager.isUserInputEnabled = true
                    }
                }
                // 返回false，表示不消费触摸事件，继续传递给其他控件
                return false
            }

            // 重写onTouchEvent方法，处理触摸事件（这里不需要实现）
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            // 重写onRequestDisallowInterceptTouchEvent方法，处理是否允许拦截触摸事件（这里不需要实现）
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }

        // 给RecyclerView添加itemTouchListener
        recyclerView.addOnItemTouchListener(itemTouchListener)

    }
}