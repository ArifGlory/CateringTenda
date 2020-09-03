package com.tapisdev.cateringtenda.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.adapter.AdapterDetailTenda
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.DetailTenda
import com.tapisdev.cateringtenda.model.Tenda
import kotlinx.android.synthetic.main.activity_list_detail_tenda.*
import kotlinx.android.synthetic.main.activity_list_detail_tenda.animation_view
import kotlinx.android.synthetic.main.activity_list_detail_tenda.fab
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class ListDetailTendaActivity : BaseActivity() {

    var TAG_GET_DETAIL_TENDA = "getDetailTenda"
    var tendaId = "none"
    var listDetailTenda = ArrayList<DetailTenda>()
    lateinit var i : Intent
    lateinit var adapter: AdapterDetailTenda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail_tenda)

        i = intent
        tendaId  = i.getStringExtra("tendaId")

        adapter = AdapterDetailTenda(listDetailTenda)

        rvDetailTenda.setHasFixedSize(true)
        rvDetailTenda.layoutManager = LinearLayoutManager(this)
        rvDetailTenda.adapter = adapter
        
        fab.setOnClickListener { 
            val i = Intent(this,AddDetailTendaActivity::class.java)
            i.putExtra("tendaId",tendaId)
            startActivity(i)
        }

        getDataDetailTenda()
    }

    fun getDataDetailTenda(){
        detailtendaRef.get().addOnSuccessListener { result ->
            listDetailTenda.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                Log.d(TAG_GET_DETAIL_TENDA, "Datanya : "+document.data)

                var detailTenda : DetailTenda = document.toObject(DetailTenda::class.java)
                detailTenda.detailTendaId = document.id
                if (detailTenda.tendaId.equals(tendaId)){
                    listDetailTenda.add(detailTenda)
                }
            }
            if (listDetailTenda.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_DETAIL_TENDA,"err : "+exception.message)
        }
    }
}
