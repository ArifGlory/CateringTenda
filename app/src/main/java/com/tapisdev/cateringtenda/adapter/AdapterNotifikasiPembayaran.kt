package com.tapisdev.cateringtenda.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.opengl.GLDebugHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.DetailCateringActivity
import com.tapisdev.cateringtenda.activity.admin.DetailPesananAdminActivity
import com.tapisdev.cateringtenda.fragment.AdminCateringFragment
import com.tapisdev.cateringtenda.model.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_detail_pesanan_admin.*
import kotlinx.android.synthetic.main.row_catering.view.*
import kotlinx.android.synthetic.main.row_catering_user.view.*
import kotlinx.android.synthetic.main.row_notifikasi.view.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class AdapterNotifikasiPembayaran(private val list:ArrayList<NotifikasiPembayaran>) : RecyclerView.Adapter<AdapterNotifikasiPembayaran.Holder>(){
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_notifikasi,parent,false))
    }
    lateinit var pDialogLoading : SweetAlertDialog
    val myDB = FirebaseFirestore.getInstance()
    val pesanananRef = myDB.collection("pesanan")
    lateinit var pesanan : Pesanan
    var TAG_GET = "getPesanan"

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        pDialogLoading = SweetAlertDialog(holder.view.tvNamaNotifikasi.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialogLoading.setTitleText("Loading..")
        pDialogLoading.setCancelable(false)

        holder.view.tvNamaNotifikasi.text = list?.get(position)?.namaNotifikasi
        holder.view.tvPemesan.text = "Oleh "+list?.get(position)?.namaPengguna
        

       holder.view.lineNotifikasi.setOnClickListener {
           pDialogLoading.show()
           pesanananRef.document(list?.get(position)?.idPesanan!!).get().addOnCompleteListener { task ->
               if (task.isSuccessful){
                   val document = task.result
                   if (document != null) {
                       if (document.exists()) {
                           Log.d(TAG_GET, "DocumentSnapshot data: " + document.data)
                           //convert doc to object
                           pesanan = document.toObject(Pesanan::class.java)!!

                           pDialogLoading.dismiss()
                           val i = Intent(holder.view.lineNotifikasi.context,DetailPesananAdminActivity::class.java)
                           i.putExtra("pesanan",pesanan as Serializable)
                           holder.view.lineNotifikasi.context.startActivity(i)

                       } else {
                           pDialogLoading.dismiss()
                           Log.d(TAG_GET, "No such document")
                       }
                   }
               }else{
                   pDialogLoading.dismiss()
                   holder.view.lineNotifikasi.context?.let { Toasty.error(it, "terjadi kesalahan, coba lagi nanti", Toast.LENGTH_SHORT, true).show() }
                   Log.d(TAG_GET,"err : "+task.exception)
               }
           }


       }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)


}