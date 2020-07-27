package com.tapisdev.cateringtenda.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.adapter.AdapterCateringUser
import com.tapisdev.cateringtenda.adapter.MyPagerAdapter
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.SharedVariable
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_catering_user.*
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class DetailPenyediaActivity : BaseActivity() {

    lateinit var penyedia : UserModel
    lateinit var i : Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_catering_user)

        i = intent
        penyedia = i.getSerializableExtra("penyedia") as UserModel
        Log.d("selectedPenyedia",SharedVariable.selectedIdPenyedia)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)


        ivBack.setOnClickListener {
            onBackPressed()
        }
        ivCart.setOnClickListener {
            val i = Intent(this,KeranjangActivity::class.java)
            startActivity(i)
        }

        updateUI()

    }

    fun updateUI(){
        tvName.setText(penyedia.name)
        if (!penyedia.foto.equals("")){
            Glide.with(this)
                .load(penyedia.foto)
                .into(ivPenyedia)
        }else{
            ivPenyedia.setImageResource(R.drawable.ic_placeholder)
        }

    }

}
