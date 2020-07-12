package com.tapisdev.cateringtenda

import android.content.Intent
import android.os.Bundle
import com.tapisdev.cateringtenda.activity.RegisterActivity
import com.tapisdev.cateringtenda.activity.admin.DashboardAdminActivity
import com.tapisdev.cateringtenda.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSignup.setOnClickListener{
            val i = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(i)
        }
        tvLogin.setOnClickListener {
            val i = Intent(applicationContext, DashboardAdminActivity::class.java)
            startActivity(i)
        }
    }
}
