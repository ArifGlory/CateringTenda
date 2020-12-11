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
import com.tapisdev.cateringtenda.activity.superadmin.DetailPenggunaActivity
import com.tapisdev.cateringtenda.fragment.AdminCateringFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.row_catering.view.*
import kotlinx.android.synthetic.main.row_catering.view.lineCatering
import kotlinx.android.synthetic.main.row_data_user.view.*
import java.io.Serializable

class AdapterPengguna(private val list:ArrayList<UserModel>) : RecyclerView.Adapter<AdapterPengguna.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_data_user,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.view.tvUsername.text = list?.get(position)?.name
        holder.view.tvEmail.text = list?.get(position)?.email

        Glide.with(holder.view.ivUser.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivUser)

        holder.view.lineCatering.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            val i = Intent(holder.view.ivUser.context,DetailPenggunaActivity::class.java)
            i.putExtra("pengguna",list.get(position) as Serializable)
            holder.view.ivUser.context.startActivity(i)
        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}