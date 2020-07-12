package com.tapisdev.cateringtenda.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvLogin.setOnClickListener {
            val i = Intent(applicationContext,MainActivity::class.java)
            startActivity(i)
        }
    }
}
