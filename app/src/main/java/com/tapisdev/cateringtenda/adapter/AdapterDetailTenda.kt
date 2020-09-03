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
import com.tapisdev.cateringtenda.activity.admin.DetailTendaActivity
import com.tapisdev.cateringtenda.fragment.AdminCateringFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.DetailTenda
import com.tapisdev.cateringtenda.model.Tenda
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.row_catering.view.*
import kotlinx.android.synthetic.main.row_detail_tenda.view.*
import java.io.Serializable

class AdapterDetailTenda(private val list:ArrayList<DetailTenda>) : RecyclerView.Adapter<AdapterDetailTenda.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_detail_tenda,parent,false))
    }

    lateinit var mUserPref : UserPreference

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        mUserPref = UserPreference(holder.view.tvNamaDetailTenda.context)

        holder.view.tvNamaDetailTenda.text = list?.get(position)?.nama
        holder.view.tvHargaDetailTenda.text = "Rp. "+list?.get(position)?.harga+" / "+list?.get(position)?.satuan


        holder.view.lineDetailTenda.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            if (mUserPref.getJenisUser().equals("admin")){
               /* val i = Intent(holder.view.lineCatering.context, DetailTendaActivity::class.java)
                i.putExtra("tenda",list.get(position) as Serializable)
                holder.view.lineCatering.context.startActivity(i)*/
            }
        }

    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}