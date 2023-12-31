package cn.edu.fzu.mytoolbox.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.edu.fzu.mytoolbox.R
import cn.edu.fzu.mytoolbox.adapter.RvGridAdapter
import cn.edu.fzu.mytoolbox.databinding.FragmentGridRecyclerViewBinding
import cn.edu.fzu.mytoolbox.entity.ItemHorizontal
import cn.edu.fzu.mytoolbox.util.setupGridRecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GridRecyclerViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GridRecyclerViewFragment : Fragment() {
    private var _binding: FragmentGridRecyclerViewBinding? = null
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
        _binding=FragmentGridRecyclerViewBinding.inflate(inflater,container,false)
        var rvGridAdapter = RvGridAdapter(R.layout.item_grid, mutableListOf())
        setupGridRecyclerView(
            binding.rvGrid, //传入recyclerView对象
            rvGridAdapter,
            listOf( //传入数据列表
                ItemHorizontal("充流量", R.drawable.ic_mobiledata, "流量告急速订购"),
                ItemHorizontal("开通自动充", R.drawable.ic_autorecharge, "专治忘充值"),
                ItemHorizontal("电子发票", R.drawable.ic_receipt, "批量开票不排队"),
                ItemHorizontal("充值记录", R.drawable.ic_bill, "可查全网记录"),
                ItemHorizontal("充流量", R.drawable.ic_mobiledata, "流量告急速订购"),
                ItemHorizontal("开通自动充", R.drawable.ic_autorecharge, "专治忘充值"),
                ItemHorizontal("电子发票", R.drawable.ic_receipt, "批量开票不排队"),
                ItemHorizontal("充值记录", R.drawable.ic_bill, "可查全网记录"),
                ItemHorizontal("充流量", R.drawable.ic_mobiledata, "流量告急速订购"),
                ItemHorizontal("开通自动充", R.drawable.ic_autorecharge, "专治忘充值"),
            ),
            3,
            10f,
            RecyclerView.VERTICAL
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
         * @return A new instance of fragment GridRecyclerViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GridRecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}