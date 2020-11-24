package com.tapisdev.cateringtenda.model

import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class SharedVariable {
    //open var selectedIdPenyedia = "-"

    companion object {
        var selectedIdPenyedia: String = "-"
        var IdPenyediaCart: String = "-"
        var totalKeranjang : Int = 0
        var listCart = ArrayList<Cart>()
        var lokasiPenyedia  = LatLng(0.0,0.0)

        fun convertRibuan(angka : Int) :String {
            var hasil = ""
            val nf = NumberFormat.getNumberInstance(Locale.GERMAN)
            val df = nf as DecimalFormat
            hasil = ""+df.format(angka)

            return  hasil
        }
    }

}