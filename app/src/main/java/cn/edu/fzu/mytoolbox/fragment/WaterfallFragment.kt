package cn.edu.fzu.mytoolbox.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.util.Util.setupWaterfall
import cn.edu.fzu.mytoolbox.adapter.RvWaterfallAdapter
import cn.edu.fzu.mytoolbox.databinding.FragmentWaterfallBinding
import cn.edu.fzu.mytoolbox.entity.ItemWaterfall

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WaterfallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaterfallFragment : Fragment() {
    private var _binding: FragmentWaterfallBinding? = null
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
        _binding=FragmentWaterfallBinding.inflate(inflater,container,false)

        var rvWaterfallAdapter= RvWaterfallAdapter(R.layout.item_waterfall,mutableListOf())
        setupWaterfall(
            binding.rvWaterfall, //传入recyclerView对象
            rvWaterfallAdapter,
            listOf( //传入数据列表
                ItemWaterfall("0", "0","电信关爱版-为老年人架桥","no",R.drawable.pic_elderly),
                ItemWaterfall("赠新人礼包", "赠美团神券","加装【副卡】，一份套餐全家用","10",R.drawable.pic_family),
                ItemWaterfall("免运费", "送配件","iPhone12 128GB 红色 双卡双待","300",R.drawable.pic_iphone),
                ItemWaterfall("赠新人礼包", "0","15GB定向流量+腾讯视频月会员卡","10",R.drawable.pic_tencentvip),
                ItemWaterfall("0", "0","电信关爱版-为老年人架桥","no",R.drawable.pic_elderly),
                ItemWaterfall("赠新人礼包", "赠美团神券","加装【副卡】，一份套餐全家用","10",R.drawable.pic_family),
                ItemWaterfall("免运费", "送配件","iPhone12 128GB 红色 双卡双待","300",R.drawable.pic_iphone),
                ItemWaterfall("赠新人礼包", "0","15GB定向流量+腾讯视频月会员卡","10",R.drawable.pic_tencentvip)
            )
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
         * @return A new instance of fragment WaterfallFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WaterfallFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}