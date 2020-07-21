package com.tapisdev.cateringtenda.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.GLDebugHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.DetailCateringActivity
import com.tapisdev.cateringtenda.fragment.AdminCateringFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.Tenda
import kotlinx.android.synthetic.main.row_catering.view.*
import kotlinx.android.synthetic.main.row_catering_user.view.*
import java.io.Serializable

class AdapterTendaUser(private val list:ArrayList<Tenda>) : RecyclerView.Adapter<AdapterTendaUser.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_tenda_user,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.tvName.text = list?.get(position)?.nama
        holder.view.tvPrice.text = "Rp. "+list?.get(position)?.harga

        Glide.with(holder.view.ivCatering.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivCatering)


    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}