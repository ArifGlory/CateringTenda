package com.tapisdev.cateringtenda.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.pengguna.KeranjangActivity
import com.tapisdev.cateringtenda.model.Cart
import kotlinx.android.synthetic.main.row_catering_user.view.tvName
import kotlinx.android.synthetic.main.row_catering_user.view.tvPrice
import kotlinx.android.synthetic.main.row_keranjang.view.*


class AdapterDetailPesanan(private val list:ArrayList<Cart>) : RecyclerView.Adapter<AdapterDetailPesanan.Holder>(){
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



    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)


}