package com.tapisdev.cateringtenda.activity.admin

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.SplashActivity
import com.tapisdev.cateringtenda.adapter.AdapterDetailPesanan
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.Cart
import com.tapisdev.cateringtenda.model.Pesanan
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_pesanan.*
import kotlinx.android.synthetic.main.activity_detail_pesanan.rvPesanan
import kotlinx.android.synthetic.main.activity_detail_pesanan.tvAlamat
import kotlinx.android.synthetic.main.activity_detail_pesanan.tvStatus
import kotlinx.android.synthetic.main.activity_detail_pesanan.tvTanggal
import kotlinx.android.synthetic.main.activity_detail_pesanan_admin.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.ArrayList

class DetailPesananAdminActivity : BaseActivity() {

    lateinit var i : Intent
    lateinit var pesanan : Pesanan
    lateinit var pengguna : UserModel
    var listCart = ArrayList<Cart>()
    lateinit var adapter: AdapterDetailPesanan
    lateinit var ivBuktiBayar : ImageView

    var TAG_GET_DETAILPESANAN = "detailpesananGET"
    var TAG_GET_USER = "penggunaGET"
    var TAG_UBAH_STATUS = "ubahStatus"
    var selectedStatus = "none"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan_admin)

        i = intent
        pesanan = i.getSerializableExtra("pesanan") as Pesanan

        adapter = AdapterDetailPesanan(listCart)
        rvPesanan.setHasFixedSize(true)
        rvPesanan.layoutManager = LinearLayoutManager(this)
        rvPesanan.adapter = adapter

        tvTanggal.setText("Tanggal pesan : "+pesanan.tanggalPesan?.let { convertDate(it) })
        tvAlamat.setText("Alamat : "+pesanan.alamat)
        tvStatus.setText(pesanan.status)

        ivBackAdmin.setOnClickListener {
            onBackPressed()
        }
        ivShowBuktiBayarAdmin.setOnClickListener {
            showDialogBuktiBayar()
        }
        tvUbahStatus.setOnClickListener { 
            showDialogUbahStatus()
        }

        getDataPemesan()
        getDataPesanan()
    }

    fun updateStatusPesanan(newStatus : String){
        showLoading(this)
        pesanananRef.document(pesanan.pesananId.toString()).update("status",newStatus).addOnCompleteListener { task ->
            dismissLoading()
            if (task.isSuccessful){
                pesanan.status = newStatus
                tvStatus.setText(pesanan.status)
                showSuccessMessage("Status berhasil diubah")
                //onBackPressed()
            }else{
                showLongErrorMessage("terjadi kesalahan, coba lagi nanti")
                Log.d(TAG_UBAH_STATUS,"err : "+task.exception)
            }
        }
    }

    fun showDialogBuktiBayar(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dlg_bukti_bayar)
        ivBuktiBayar = dialog.findViewById(R.id.ivBuktiBayar) as ImageView
        val tvClose = dialog.findViewById(R.id.tvClose) as TextView
        val tvAdd = dialog.findViewById(R.id.tvAdd) as TextView

        if(pesanan.buktiBayar.equals("")){
            ivBuktiBayar.setImageResource(R.drawable.ic_placeholder)
            tvAdd.setText("Belum mengirim bukti bayar")
        }else{
            Glide.with(this)
                .load(pesanan.buktiBayar)
                .into(ivBuktiBayar)
            tvAdd.setText("")
        }

        tvClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showDialogUbahStatus(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dlg_ubah_status_pesan)
        val spStatus  = dialog.findViewById(R.id.spStatus) as Spinner
        val tvCancel = dialog.findViewById(R.id.tvCancel) as TextView
        val tvAdd = dialog.findViewById(R.id.tvAdd) as TextView

        spStatus.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d(TAG_UBAH_STATUS, "jenis nya "+ parent?.getItemAtPosition(position).toString())
                var selected = parent?.getItemAtPosition(position).toString()
                if (selected.equals("Pilih Status Pesanan")){
                    selectedStatus = "none"
                }else{
                    selectedStatus = selected
                }

                /*else if (selected.equals("menunggu konfirmasi")){
                    selectedStatus = "menunggu konfirmasi"
                }else if (selected.equals("pesanan diproses")){
                    selectedStatus = "pesanan diproses"
                }else if (selected.equals("pesanan selesai")){
                    selectedStatus = "pesanan selesai"
                }else if (selected.equals("pesanan ditolak")){
                    selectedStatus = "pesanan ditolak"
                }else if (selected.equals("sedang dikirim ke pemesan")){
                    selectedStatus = "sedang dikirim ke pemesan"
                }*/
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvAdd.setOnClickListener { 
            if(selectedStatus.equals("none")){
                showErrorMessage("anda belum memilih status")
            }else{
                updateStatusPesanan(selectedStatus)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
    
    fun getDataPemesan(){
        userRef.document(pesanan.idUser.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG_GET_USER, "DocumentSnapshot data: " + document.data)
                        //convert doc to object
                        pengguna = document.toObject(UserModel::class.java)!!
                        tvPemesan.setText(pengguna.name)

                    } else {
                        Log.d(TAG_GET_USER, "No such document")
                    }
                }
            }else{
                showErrorMessage("Pengguna tidak ditemukan")
                Log.d(TAG_GET_USER,"err : "+task.exception)
            }
        }
    }

    fun getDataPesanan(){
        detailpesananRef.get().addOnSuccessListener { result ->
            listCart.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var cart : Cart = document.toObject(Cart::class.java)
                if (cart.idUser.equals(pesanan.idUser)){
                    listCart.add(cart)
                }
            }
            countTotalPrice()
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_DETAILPESANAN,"err : "+exception.message)
        }
    }

    fun countTotalPrice(){
        var totalPrice = 0
        for (c in 0 until listCart.size){
            var subtotal = listCart.get(c).harga?.times(listCart.get(c).jumlah!!)
            if (subtotal != null) {
                totalPrice += subtotal
            }
        }
        tvTotalPriceAdmin.setText("Total Bayar : Rp. "+totalPrice)
    }
}
