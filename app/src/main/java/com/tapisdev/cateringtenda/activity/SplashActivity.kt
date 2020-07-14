package com.tapisdev.cateringtenda.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.DashboardAdminActivity
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.UserPreference

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mUserPref = UserPreference(this)

        if (auth.currentUser != null){
            Log.d("userpref"," jenis user : "+mUserPref.getJenisUser())
            if (mUserPref.getJenisUser() != null){
                if (mUserPref.getJenisUser().equals("admin")){
                val i = Intent(applicationContext,DashboardAdminActivity::class.java)
                startActivity(i)
                }else if(mUserPref.getJenisUser().equals("pengguna")){
                    showInfoMessage("login sebagai pengguna masih dibuat ya..")
                    val i = Intent(applicationContext,MainActivity::class.java)
                    startActivity(i)
                }else{
                    val i = Intent(applicationContext,MainActivity::class.java)
                    startActivity(i)
                }
            }else{
                val i = Intent(applicationContext,MainActivity::class.java)
                startActivity(i)
            }
        }else{
            val i = Intent(applicationContext,MainActivity::class.java)
            startActivity(i)
        }
    }
}
