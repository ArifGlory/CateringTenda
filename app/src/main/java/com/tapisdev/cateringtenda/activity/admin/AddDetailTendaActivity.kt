package com.tapisdev.cateringtenda.activity.admin

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.DetailTenda
import com.tapisdev.cateringtenda.model.Tenda
import com.tapisdev.cateringtenda.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_add_catering.*
import kotlinx.android.synthetic.main.activity_add_catering.edFullName
import kotlinx.android.synthetic.main.activity_add_catering.edHarga
import kotlinx.android.synthetic.main.activity_add_catering.tvAdd
import kotlinx.android.synthetic.main.activity_add_detail_tenda.*

class AddDetailTendaActivity : BaseActivity(),PermissionHelper.PermissionListener {

    var TAG_SIMPAN = "simpanDetailTenda"
    var tendaID = "none"
    lateinit var detailTenda : DetailTenda

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null

    lateinit var i : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_detail_tenda)

        i = intent
        tendaID  = i.getStringExtra("tendaId")
        storageReference = FirebaseStorage.getInstance().reference.child("images")

        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)

        tvAdd.setOnClickListener {
            checkValidation()
        }
    }

    fun checkValidation(){
        var getName = edFullName.text.toString()
        var getHarga = edHarga.text.toString()
        var getSatuan = edSatuan.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        } else if (getHarga.equals("") || getHarga.length == 0){
            showErrorMessage("Harga Belum diisi")
        } else if (getSatuan.equals("") || getSatuan.length == 0){
            showErrorMessage("Satuan Belum diisi")
        }else {
            var harga = Integer.parseInt(getHarga)
            detailTenda = DetailTenda("",
                tendaID,
                getName,
                harga,
                getSatuan,
                auth.currentUser?.uid
            )
            showLoading(this)
            saveDetailTenda()
        }
    }

    fun saveDetailTenda(){
        pDialogLoading.setTitleText("menyimpan data..")
        showInfoMessage("Sedang menyimpan ke database..")
        detailtendaRef.document().set(detailTenda).addOnCompleteListener{
                task ->
            dismissLoading()
            if (task.isSuccessful){
                showSuccessMessage("Data berhasil ditambahkan")
                onBackPressed()
            }else{
                showLongErrorMessage("Penyimpanan data gagal")
                Log.d(TAG_SIMPAN,"err : "+task.exception)
            }
        }
    }

    override fun onPermissionCheckDone() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
}
