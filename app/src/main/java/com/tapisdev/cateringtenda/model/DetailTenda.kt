package com.tapisdev.cateringtenda.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DetailTenda(
    var detailTendaId: String? = "",
    var tendaId: String? = "",
    var nama: String? = "",
    var harga: Int? = 0,
    var satuan: String? = "",
    var idAdmin: String? = ""
) : Parcelable, java.io.Serializable