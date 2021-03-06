package com.tapisdev.cateringtenda.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Tenda(
    var tendaId: String? = "",
    var nama: String? = "",
    var harga: Int? = 0,
    var foto: String? = "",
    var deksripsi: String? = "",
    var idAdmin: String? = "",
    var satuan: String? = "",
    var stok: Int? = 0
) : Parcelable, java.io.Serializable