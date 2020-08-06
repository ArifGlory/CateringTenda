package com.tapisdev.cateringtenda.activity.pengguna

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.SharedVariable
import kotlinx.android.synthetic.main.activity_konfirmasi_pesan.*

class KonfirmasiPesanActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_pesan)

        tvPesan.setOnClickListener {
            checkValidation()
        }

        updateUI()
    }


    fun checkValidation(){
        var getAlamat = edAlamat.text.toString()

        if (getAlamat.equals("") || getAlamat.length == 0){
            showErrorMessage("Alamat harus diisi")
        }else{

        }
    }

    fun updateUI(){
        tvTotalPrice.setText("Rp. "+SharedVariable.totalKeranjang)
    }
}
