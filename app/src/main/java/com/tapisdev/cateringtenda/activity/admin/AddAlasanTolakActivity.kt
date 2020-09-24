package com.tapisdev.cateringtenda.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.Cart
import kotlinx.android.synthetic.main.activity_add_alasan_tolak.*

class AddAlasanTolakActivity : BaseActivity() {

    lateinit var i : Intent
    lateinit var cart : Cart
    var TAG_TOLAK = "tolak"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alasan_tolak)

        i = intent
        cart = i.getSerializableExtra("cart") as Cart

        tvTitle.setText("Alasan tolak "+cart.nama)

        tvAddPenolakan.setOnClickListener {
            var alasan = edAlasanPenolakan.text.toString()

            if (alasan.equals("") || alasan.length == 0){
                showErrorMessage("Alasan harus diisi")
            }else{
                showLoading(this)
                detailpesananRef.document(cart.cartId.toString()).update("alasanPenolakan",alasan)
                detailpesananRef.document(cart.cartId.toString()).update("status","tolak").addOnCompleteListener { task ->
                    dismissLoading()
                    if (task.isSuccessful){
                        showSuccessMessage("Penolakan berhasil")
                        onBackPressed()
                    }else{
                        showLongErrorMessage("terjadi kesalahan : "+task.exception)
                        Log.d(TAG_TOLAK,"err : "+task.exception)
                    }
                }
            }
        }
    }
}
