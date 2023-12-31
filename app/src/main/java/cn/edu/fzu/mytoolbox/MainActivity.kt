package cn.edu.fzu.mytoolbox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.edu.fzu.mytoolbox.databinding.ActivityMainBinding
import cn.edu.fzu.mytoolbox.entity.GetFeedTabData
import cn.edu.fzu.mytoolbox.entity.GetFeedTabData.TabListBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gyf.immersionbar.ImmersionBar
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //设置沉浸式状态栏
        ImmersionBar.with(this)
            .statusBarDarkFont(true) //设置状态栏字体为深色
            .init()

        // 点击跳转
        binding.tvRvActivity.setOnClickListener {
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }

        binding.tvRechargeSuccessActivity.setOnClickListener {
            val intent = Intent(this, RechargeSuccessActivity::class.java)
            startActivity(intent)
        }


    }


}