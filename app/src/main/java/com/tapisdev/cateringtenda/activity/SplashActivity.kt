package com.tapisdev.cateringtenda.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.DashboardAdminActivity
import com.tapisdev.cateringtenda.activity.pengguna.HomeUserActivity
import com.tapisdev.cateringtenda.activity.superadmin.HomeSuperadminActivity
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.UserPreference

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mUserPref = UserPreference(this)

        if (auth.currentUser != null){

            settingsRef.document("maintenance").get().addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    var mode = task.result?.get("mode")
                    if (mode != null) {
                        if (mode.equals("1")){
                            auth.signOut()
                            val i = Intent(applicationContext,MaintenanceActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    }
                }
            }

            Log.d("userpref"," jenis user : "+mUserPref.getJenisUser())
            if (mUserPref.getJenisUser() != null){
                if (mUserPref.getJenisUser().equals("admin")){
                val i = Intent(applicationContext,DashboardAdminActivity::class.java)
                startActivity(i)
                }else if(mUserPref.getJenisUser().equals("pengguna")){
                    val i = Intent(applicationContext,HomeUserActivity::class.java)
                    startActivity(i)
                }else if(mUserPref.getJenisUser().equals("superadmin")){
                    val i = Intent(applicationContext,HomeSuperadminActivity::class.java)
                    startActivity(i)
                }
                else{
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
