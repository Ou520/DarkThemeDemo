package com.ouwenbin.darkthemedemo

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var btn_theme:Button
    private lateinit var tv_theme:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_theme = findViewById(R.id.btn_theme)
        tv_theme = findViewById(R.id.tv_theme)

        initData()
        initListener()




    }

    private fun initData() {

        if (isDarkTheme(this)) {
            tv_theme.text = "当前是深色主题"
        } else {
            tv_theme.text = "当前是浅色主题"
        }
    }

    private fun initListener() {
        //浅色与深色主题切换按钮
        btn_theme.setOnClickListener {

            if (isDarkTheme(this)) {

                /*
                * setDefaultNightMode()方法接收一个mode参数,有以下值可供选择：
                *
                * MODE_NIGHT_FOLLOW_SYSTEM：默认模式，表示让当前应用程序跟随系统设置来决定使用浅色主题还是深色主题。
                * MODE_NIGHT_YES：脱离系统设置，强制让当前应用程序使用深色主题
                * MODE_NIGHT_NO：脱离系统设置，强制让当前应用程序使用浅色主题。
                * MODE_NIGHT_AUTO_BATTERY：根据手机的电池状态来决定使用浅色主题还是深色主题，如果开启了节点模式，则使用深色主题
                * */
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                tv_theme.text = "当前是深色主题"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                tv_theme.text = "当前是浅色主题"
            }
        }
    }


    //判断当前的主题样式
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                Toast.makeText(this,"正在使用浅色主题",Toast.LENGTH_SHORT).show()
            } // 夜间模式未启用，使用浅色主题
            Configuration.UI_MODE_NIGHT_YES -> {

                Toast.makeText(this,"正在使用深色主题",Toast.LENGTH_SHORT).show()
            } // 夜间模式启用，使用深色主题
        }
    }


}