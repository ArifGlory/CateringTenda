package com.tapisdev.cateringtenda.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.DetailCateringActivity
import com.tapisdev.cateringtenda.fragment.AdminCateringFragment
import com.tapisdev.cateringtenda.model.Cart
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.NotifikasiPembayaran
import com.tapisdev.cateringtenda.model.SharedVariable
import es.dmoral.toasty.Toasty
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

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.tvNamaNotifikasi.text = list?.get(position)?.namaNotifikasi
        holder.view.tvPemesan.text = "Oleh "+list?.get(position)?.namaPengguna
        

       holder.view.lineNotifikasi.setOnClickListener {

       }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)


}