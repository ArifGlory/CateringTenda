package com.tapisdev.cateringtenda.activity.pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tapisdev.cateringPesanan.adapter.AdapterPesananUser
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.adapter.AdapterDetailPesanan
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.Cart
import com.tapisdev.cateringtenda.model.Pesanan
import com.tapisdev.cateringtenda.model.UserModel
import kotlinx.android.synthetic.main.activity_detail_pesanan.*
import java.util.*

class DetailPesananActivity : BaseActivity() {

    lateinit var i : Intent
    lateinit var pesanan : Pesanan
    var listCart = ArrayList<Cart>()
    lateinit var adapter: AdapterDetailPesanan

    var TAG_GET_DETAILPESANAN = "detailpesananGET"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan)

        i = intent
        pesanan = i.getSerializableExtra("pesanan") as Pesanan

        adapter = AdapterDetailPesanan(listCart)
        rvPesanan.setHasFixedSize(true)
        rvPesanan.layoutManager = LinearLayoutManager(this)
        rvPesanan.adapter = adapter

        tvTanggal.setText("Tanggal pesan : "+pesanan.tanggalPesan?.let { convertDate(it) })
        tvAlamat.setText("Alamat : "+pesanan.alamat)
        tvStatus.setText(pesanan.status)

        ivBuktiBayar.setOnClickListener {

        }

        getDataPesanan()
    }

    fun getDataPesanan(){
        detailpesananRef.get().addOnSuccessListener { result ->
            listCart.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var cart : Cart = document.toObject(Cart::class.java)
                if (cart.idUser.equals(auth.currentUser?.uid)){
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
        tvTotalPrice.setText("Total Bayar : Rp. "+totalPrice)
    }
}
