package com.tapisdev.cateringtenda.adapter

import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddAlasanTolakActivity
import com.tapisdev.cateringtenda.activity.pengguna.KeranjangActivity
import com.tapisdev.cateringtenda.model.Cart
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.row_catering_user.view.*
import kotlinx.android.synthetic.main.row_catering_user.view.tvName
import kotlinx.android.synthetic.main.row_catering_user.view.tvPrice
import kotlinx.android.synthetic.main.row_detail_pesanan.view.*
import kotlinx.android.synthetic.main.row_keranjang.view.*
import kotlinx.android.synthetic.main.row_keranjang.view.ivKeranjang
import kotlinx.android.synthetic.main.row_keranjang.view.tvJenis
import kotlinx.android.synthetic.main.row_keranjang.view.tvJumlahDipesan
import java.io.Serializable


class AdapterDetailPesanan(private val list:ArrayList<Cart>) : RecyclerView.Adapter<AdapterDetailPesanan.Holder>(){
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mUserPref : UserPreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.row_detail_pesanan,parent,false))
    }

    override fun getItemCount(): Int = list?.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        mUserPref = UserPreference(holder.view.ivTolak.context)

        holder.view.tvName.text = list?.get(position)?.nama
        holder.view.tvJenis.text = list?.get(position)?.jenis
        holder.view.tvJumlahDipesan.text = "Jumlah Dipesan : "+list?.get(position)?.jumlah
        holder.view.tvPrice.text = "Rp. "+list?.get(position)?.harga

        if (mUserPref.getJenisUser().equals("pengguna")){
            holder.view.ivTolak.visibility = View.INVISIBLE
            holder.view.ivTolak.isEnabled = false
        }

        if (list?.get(position)?.status.equals("tolak")){
            holder.view.tvStatusDetailPesanan.visibility = View.VISIBLE
            holder.view.tvStatusDetailPesanan.setText("Ditolak")

            holder.view.ivTolak.visibility = View.INVISIBLE
            holder.view.ivTolak.isEnabled = false
        }


        Glide.with(holder.view.ivKeranjang.context)
            .load(list?.get(position)?.foto)
            .into(holder.view.ivKeranjang)

        holder.view.rlGambarStatus.setOnClickListener {
            if (list?.get(position)?.status.equals("tolak")){
                showDialog(holder,list?.get(position)?.alasanPenolakan.toString())
            }
        }
        holder.view.ivTolak.setOnClickListener {
            Log.d("adapterIsi",""+list.get(position).toString())
            val i = Intent(holder.view.ivTolak.context,AddAlasanTolakActivity::class.java)
            i.putExtra("cart",list.get(position) as Serializable)
            holder.view.ivTolak.context.startActivity(i)
        }



    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    private fun showDialog(holder: AdapterDetailPesanan.Holder, alasan: String) {
        val dialog = Dialog(holder.view.ivTolak.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dlg_alasan_penolakan)
        val tvAlasan = dialog.findViewById(R.id.tvAlasan) as TextView
        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView


        tvAlasan.setText(alasan)
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


}