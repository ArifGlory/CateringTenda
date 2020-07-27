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
import com.tapisdev.cateringtenda.model.SharedVariable
import com.tapisdev.cateringtenda.model.Tenda
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.row_catering.view.*
import kotlinx.android.synthetic.main.row_catering_user.view.*
import kotlinx.android.synthetic.main.row_catering_user.view.ivCatering
import kotlinx.android.synthetic.main.row_catering_user.view.tvName
import kotlinx.android.synthetic.main.row_catering_user.view.tvPrice
import kotlinx.android.synthetic.main.row_keranjang.view.*
import kotlinx.android.synthetic.main.row_tenda_user.view.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class AdapterKeranjang(private val list:ArrayList<Cart>) : RecyclerView.Adapter<AdapterKeranjang.Holder>(){
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_keranjang,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.tvName.text = list?.get(position)?.nama
        holder.view.tvJenis.text = list?.get(position)?.jenis
        holder.view.tvPrice.text = "Rp. "+list?.get(position)?.harga+ " @"+list?.get(position).jumlah+" paket"

        Glide.with(holder.view.ivKeranjang.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivKeranjang)

        holder.view.ivDeleteCartItem.setOnClickListener {

        }



    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)


}