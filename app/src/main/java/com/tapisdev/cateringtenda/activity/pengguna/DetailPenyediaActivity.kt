package com.tapisdev.cateringtenda.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.adapter.AdapterCateringUser
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_catering_user.*
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class DetailPenyediaActivity : BaseActivity() {

    lateinit var penyedia : UserModel
    lateinit var i : Intent

    var TAG_GET_CATERING = "getCatering"
    lateinit var adapter: AdapterCateringUser
    var listCatering = ArrayList<Catering>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_catering_user)

        i = intent
        penyedia = i.getSerializableExtra("penyedia") as UserModel
        adapter = AdapterCateringUser(listCatering)
        rvCatering.setHasFixedSize(true)
        rvCatering.layoutManager = GridLayoutManager(this,2)
        rvCatering.adapter = adapter

        ivBack.setOnClickListener {
            onBackPressed()
        }

        updateUI()
        getDataMyCatering()

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

    fun getDataMyCatering(){
        cateringRef.get().addOnSuccessListener { result ->
            listCatering.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var catering : Catering = document.toObject(Catering::class.java)
                catering.cateringId = document.id
                if (catering.idAdmin.equals(penyedia.uId)){
                    listCatering.add(catering)
                }
            }
            if (listCatering.size == 0){
                tvNoDataFound.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_CATERING,"err : "+exception.message)
        }
    }
}
