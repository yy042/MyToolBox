package cn.edu.fzu.mytoolbox.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.edu.fzu.mytoolbox.adapter.FeedWaterfallAdapter
import cn.edu.fzu.mytoolbox.databinding.FragmentMultiViewWaterfallBinding
import cn.edu.fzu.mytoolbox.entity.GetFeedListData
import cn.edu.fzu.mytoolbox.util.FeedView
import cn.edu.fzu.mytoolbox.util.Util
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
class MultiViewWaterfallFragment : Fragment() {

    private var _binding: FragmentMultiViewWaterfallBinding? = null
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
        _binding= FragmentMultiViewWaterfallBinding.inflate(inflater,container,false)

        // 从本地json文件中读取json字符串
        val json = requireActivity().assets.open("feed.json").bufferedReader().use { it.readText() }
        // 使用Gson对象将json字符串转换为TabListBean对象列表
        val gson = Gson()
        val GetFeedListData = gson.fromJson(json, GetFeedListData::class.java)
        val feedList: List<GetFeedListData.FeedListBean> = GetFeedListData.feedList

        val rvWaterfallAdapter= FeedWaterfallAdapter(mutableListOf(),context as FeedView.OnFeedClickListener)
        Util.setupWaterfall(
            binding.rvFeedWaterfall, //传入recyclerView对象
            rvWaterfallAdapter,
            feedList
        )

        binding.rvFeedWaterfall.isNestedScrollingEnabled=true

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
}