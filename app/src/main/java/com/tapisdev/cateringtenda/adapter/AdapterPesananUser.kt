package com.tapisdev.cateringPesanan.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.pengguna.DetailPesananActivity
import com.tapisdev.cateringtenda.model.Pesanan
import kotlinx.android.synthetic.main.row_catering.view.ivFoodCart
import kotlinx.android.synthetic.main.row_catering.view.lineCatering
import kotlinx.android.synthetic.main.row_pesanan_user.view.*
import java.io.Serializable
import java.text.SimpleDateFormat


class AdapterPesananUser(private val list:ArrayList<Pesanan>) : RecyclerView.Adapter<AdapterPesananUser.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_pesanan_user,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        var tanggalPesan = list?.get(position)?.tanggalPesan?.let { convertDate(it) }
        holder.view.tvTanggal.text = "Pesanan tanggal "+tanggalPesan
        holder.view.tvAlamat.text = "Alamat Kirim di "+list?.get(position)?.alamat
        holder.view.ivFoodCart.setImageResource(R.drawable.ic_appicon)

        holder.view.lineCatering.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            val i = Intent(holder.view.lineCatering.context, DetailPesananActivity::class.java)
            i.putExtra("pesanan",list.get(position) as Serializable)
            holder.view.lineCatering.context.startActivity(i)
        }

    }

    fun convertDate(tanggal : String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val output = formatter.format(parser.parse(tanggal))

        return output
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}