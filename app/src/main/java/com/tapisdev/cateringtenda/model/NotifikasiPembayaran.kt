package com.tapisdev.cateringtenda.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class NotifikasiPembayaran(
    var notifikasiId: String? = "",
    var namaNotifikasi: String? = "",
    var namaPengguna: String? = "",
    var tanggal: String? = "",
    var idPengguna: String? = "",
    var idPesanan: String? = "",
    var idAdmin: String? = ""
) : Parcelable, java.io.Serializable