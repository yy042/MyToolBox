package cn.edu.fzu.mytoolbox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.edu.fzu.mytoolbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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