package com.tapisdev.cateringtenda.adapter

import android.app.Activity
import android.content.Context
import android.opengl.GLDebugHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.fragment.AdminCateringFragment
import com.tapisdev.cateringtenda.model.Catering
import kotlinx.android.synthetic.main.row_catering.view.*

class AdapterCatering(private val list:ArrayList<Catering>) : RecyclerView.Adapter<AdapterCatering.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_catering,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.tvCateringName.text = list?.get(position)?.nama
        holder.view.tvDeskripsi.text = list?.get(position)?.deksripsi

        Glide.with(holder.view.ivFoodCart.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivFoodCart)

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}