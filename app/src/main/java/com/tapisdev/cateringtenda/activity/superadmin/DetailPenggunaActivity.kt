package com.tapisdev.cateringtenda.activity.superadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_pengguna.*

class DetailPenggunaActivity : BaseActivity() {

    lateinit var i : Intent
    lateinit var pengguna : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pengguna)

        i = intent
        pengguna = i.getSerializableExtra("pengguna") as UserModel

        updateUI()
    }

    fun updateUI(){
        tvName.setText("Nama \n "+pengguna.name)
        tvEmail.setText("Email \n"+pengguna.email)
        tvPhone.setText("Telepon \n"+pengguna.phone)

        if (pengguna.jenis.equals("admin")){
            tvJenis.setText("Jenis Pengguna \n"+"Penyedia")
        }else if (pengguna.jenis.equals("pengguna")){
            tvJenis.setText("Jenis Pengguna \n"+"User")
        }

        if (!pengguna.foto.equals("")){
            Glide.with(this)
                .load(pengguna.foto)
                .into(ivPenyedia)
        }else{
            ivPenyedia.setImageResource(R.drawable.ic_placeholder)
        }
    }
}
