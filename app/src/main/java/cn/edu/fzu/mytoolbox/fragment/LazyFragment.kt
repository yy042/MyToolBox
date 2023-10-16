package cn.edu.fzu.mytoolbox.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class LazyFragment : Fragment(){
    private lateinit var mContext: Context
    private var isFirstLoad = true // 是否第一次加载

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(mContext).inflate(getContentViewId(), null)
        initView(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            // 将数据加载逻辑放到onResume()方法中
            initData()
            initEvent()
            isFirstLoad = false
        }
    }

    /**
     * 设置布局资源id
     *
     * @return
     */
    protected abstract fun getContentViewId(): Int

    /**
     * 初始化视图
     *
     * @param view
     */
    protected open fun initView(view: View) {}

    /**
     * 初始化数据
     */
    protected open fun initData() {}

    /**
     * 初始化事件
     */
    protected open fun initEvent() {}
}