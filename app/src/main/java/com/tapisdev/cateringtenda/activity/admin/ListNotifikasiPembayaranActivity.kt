package com.tapisdev.cateringtenda.activity.admin

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.adapter.AdapterNotifikasiPembayaran
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.NotifikasiPembayaran
import kotlinx.android.synthetic.main.activity_detail_tenda.*
import kotlinx.android.synthetic.main.activity_list_notifikasi_pembayaran.*

class ListNotifikasiPembayaranActivity : BaseActivity() {

    lateinit var adapter:AdapterNotifikasiPembayaran

    var listNotifikasi = ArrayList<NotifikasiPembayaran>()
    var TAG_GET = "getNotifikasi"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notifikasi_pembayaran)

        adapter = AdapterNotifikasiPembayaran(listNotifikasi)

        rvNotifikasi.setHasFixedSize(true)
        rvNotifikasi.layoutManager = LinearLayoutManager(this)
        rvNotifikasi.adapter = adapter

        edSearchNotifikasi.doOnTextChanged { text, start, before, count ->
            var query = text.toString().toLowerCase().trim()
            var listSearchCatering = ArrayList<NotifikasiPembayaran>()

            for (c in 0 until listNotifikasi.size){
                var namaCatering = listNotifikasi.get(c).namaPengguna.toString().toLowerCase().trim()
                if (namaCatering.contains(query)){
                    listSearchCatering.add(listNotifikasi.get(c))
                }
            }

            adapter = AdapterNotifikasiPembayaran(listSearchCatering)
            rvNotifikasi.layoutManager = LinearLayoutManager(this)
            rvNotifikasi.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        getDataMyNotifikasi()
    }

    fun getDataMyNotifikasi(){
        notifikasiRef.orderBy("tanggal",Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            listNotifikasi.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var notifikasi : NotifikasiPembayaran = document.toObject(NotifikasiPembayaran::class.java)
                notifikasi.notifikasiId = document.id
                if (notifikasi.idAdmin.equals(auth.currentUser?.uid)){
                    listNotifikasi.add(notifikasi)
                }
            }
            if (listNotifikasi.size == 0){
                animation_view.setAnimation(R.raw.empty_box)
                animation_view.playAnimation()
                animation_view.loop(false)
            }else{
                animation_view.visibility = View.INVISIBLE
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET,"err : "+exception.message)
        }
    }

    override fun onResume() {
        super.onResume()
        getDataMyNotifikasi()
    }
}
