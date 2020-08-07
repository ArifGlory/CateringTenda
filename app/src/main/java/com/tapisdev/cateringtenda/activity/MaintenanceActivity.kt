package com.tapisdev.cateringtenda.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity

class MaintenanceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val i = Intent(applicationContext,MainActivity::class.java)
        startActivity(i)
        finish()
    }
}
