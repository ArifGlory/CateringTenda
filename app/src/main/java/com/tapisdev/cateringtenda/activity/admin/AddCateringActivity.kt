package com.tapisdev.cateringtenda.activity.admin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_add_catering.*
import java.io.IOException
import java.util.*

class AddCateringActivity : BaseActivity(), PermissionHelper.PermissionListener {

    var TAG_SIMPAN = "simpanCatering"
    lateinit var cateringModel : Catering

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var  permissionHelper : PermissionHelper
    lateinit var fotoBitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_catering)

        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)

        ivCatering.setOnClickListener {
            launchGallery()
        }
        tvAdd.setOnClickListener {
            checkValidation()
        }
    }

    private fun launchGallery() {
        var listPermissions: MutableList<String> = ArrayList()
        listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                fotoBitmap = bitmap
                ivCatering.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun checkValidation(){
        var getName = edFullName.text.toString()
        var getHarga = edHarga.text.toString()
        var getDeskripsi = edDeskripsi.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        } else if (getHarga.equals("") || getHarga.length == 0){
            showErrorMessage("Harga Belum diisi")
        } else if (getDeskripsi.equals("") || getDeskripsi.length == 0){
            showErrorMessage("Deskripsi Belum diisi")
        }else {
            var harga = Integer.parseInt(getHarga)
            cateringModel = Catering("",
                getName,
                harga,
                "",
                getDeskripsi,
                auth.currentUser?.uid
            )
            uploadCatering()
        }
    }

    fun uploadCatering(){
        showLoading(this)
        if(filePath != null){
            val ref = storageReference?.child("images/"+UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    dismissLoading()
                    showErrorMessage("terjadi kesalahan, coba lagi nanti")
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener{
                task ->
                if (task.isSuccessful){
                    showInfoMessage("upload gambar sukses..")
                    val downloadUri = task.result
                    Log.d(TAG_SIMPAN,"downlaod uri : "+downloadUri.toString())

                    cateringModel.foto = downloadUri.toString()
                    saveCatering()
                }else{
                    dismissLoading()
                    showErrorMessage("Gagal mengupload gambar")
                    Log.d(TAG_SIMPAN,"err : "+task.exception)
                }
            }
        }else{
            dismissLoading()
            showErrorMessage("Anda belum memilih file")
        }
    }

    fun saveCatering(){
        showInfoMessage("Sedang menyimpan ke database..")
        cateringRef.document().set(cateringModel).addOnCompleteListener{
            task ->
            dismissLoading()
            if (task.isSuccessful){
                showSuccessMessage("Data catering berhasil ditambahkan")
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
