package com.tapisdev.cateringtenda.activity.admin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.SharedVariable
import com.tapisdev.cateringtenda.model.UserPreference
import com.tapisdev.cateringtenda.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_add_catering.*
import kotlinx.android.synthetic.main.activity_add_catering.edFullName
import kotlinx.android.synthetic.main.activity_add_catering.ivCatering
import kotlinx.android.synthetic.main.activity_edit_profil_admin.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class EditProfilAdminActivity : BaseActivity(),PermissionHelper.PermissionListener {

    var TAG_EDIT = "ubahProfil"
    var alamat = "none"
    var latlon = "none"

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil_admin)

        storageReference = FirebaseStorage.getInstance().reference.child("images")
        mUserPref = UserPreference(this)

        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)

        ivCatering.setOnClickListener {
            launchGallery()
        }
        tvUpdate.setOnClickListener {
            checkValidation()
        }
        tvChooseLocation.setOnClickListener {
            val i = Intent(this,MapsActivity::class.java)
            startActivity(i)
        }

        updateUI()
    }

    fun updateUI(){
        edFullName.setText(mUserPref.getName())
        edPhone.setText(mUserPref.getPhone())
        if (mUserPref.getAlamat().equals("none") || mUserPref.getAlamat()?.length  == 0){
            edAlamat.setText("Alamat belum dipilih")
        }else{
            edAlamat.setText(mUserPref.getAlamat())
        }

        if (mUserPref.getFoto().equals("")){
            ivCatering.setImageResource(R.drawable.ic_placeholder)
        }else{
            Glide.with(this)
                .load(mUserPref.getFoto())
                .into(ivCatering)
        }
    }

    override fun onPermissionCheckDone() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    fun checkValidation(){
        var getName = edFullName.text.toString()
        var getPhone = edPhone.text.toString()
        var getAlamat = edAlamat.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        } else if (getPhone.equals("") || getPhone.length == 0){
            showErrorMessage("Telepon Belum diisi")
        }else if (getAlamat.equals("") || getAlamat.length == 0){
            showErrorMessage("Alamat Belum diisi")
        }
        else if (fileUri == null) {
            updateDataOnly(getName,getPhone,getAlamat)
        }else {
            uploadAndUpdate(getName,getPhone,getAlamat)
        }
    }

    fun updateDataOnly(name : String,phone : String,alamat : String){
        showLoading(this)
        userRef.document(auth.currentUser?.uid.toString()).update("name",name)
        userRef.document(auth.currentUser?.uid.toString()).update("alamat",alamat)
        userRef.document(auth.currentUser?.uid.toString()).update("latlon",latlon)
        userRef.document(auth.currentUser?.uid.toString()).update("phone",phone).addOnCompleteListener { task ->
            dismissLoading()
            if (task.isSuccessful){
                showSuccessMessage("Ubah data berhasil")
                mUserPref.saveName(name)
                mUserPref.savePhone(phone)
                mUserPref.saveAlamat(alamat)
                mUserPref.saveLatlon(latlon)
                onBackPressed()
            }else{
                showErrorMessage("terjadi kesalahan : "+task.exception)
                Log.d(TAG_EDIT,"err : "+task.exception)
            }
        }
    }

    fun uploadAndUpdate(name : String,phone : String,alamat: String){
        showLoading(this)
        if (fileUri != null){
            val baos = ByteArrayOutputStream()
            fotoBitmap?.compress(Bitmap.CompressFormat.JPEG,50,baos)
            val data: ByteArray = baos.toByteArray()

            val fileReference = storageReference!!.child(System.currentTimeMillis().toString())
            val uploadTask = fileReference.putBytes(data)

            uploadTask.addOnFailureListener {
                    exception -> Log.d(TAG_EDIT, exception.toString())
            }.addOnSuccessListener {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                showSuccessMessage("Image Berhasil di upload")
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                    }

                    fileReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val url = downloadUri!!.toString()
                        Log.d(TAG_EDIT,"download URL : "+ downloadUri.toString())// This is the one you should store

                        userRef.document(auth.currentUser?.uid.toString()).update("name",name)
                        userRef.document(auth.currentUser?.uid.toString()).update("phone",name)
                        userRef.document(auth.currentUser?.uid.toString()).update("alamat",alamat)
                        userRef.document(auth.currentUser?.uid.toString()).update("latlon",latlon)
                        userRef.document(auth.currentUser?.uid.toString()).update("foto",url).addOnCompleteListener { task ->
                            dismissLoading()
                            if (task.isSuccessful){
                                showSuccessMessage("Ubah data berhasil")
                                mUserPref.saveName(name)
                                mUserPref.savePhone(phone)
                                mUserPref.saveFoto(url)
                                mUserPref.saveAlamat(alamat)
                                mUserPref.saveLatlon(latlon)
                                onBackPressed()
                            }else{
                                showErrorMessage("terjadi kesalahan : "+task.exception)
                                Log.d(TAG_EDIT,"err : "+task.exception)
                            }
                        }

                    } else {
                        dismissLoading()
                        showErrorMessage("Terjadi kesalahan, coba lagi nanti")
                    }
                }
            }.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                pDialogLoading.setTitleText("Uploaded " + progress.toInt() + "%...")
            }

        }else{
            dismissLoading()
            showErrorMessage("Belum memilih gambar")
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
            fileUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                fotoBitmap = bitmap
                ivCatering.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getCompleteAddressString(
        LATITUDE: Double,
        LONGITUDE: Double
    ): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? =
                geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.d("address", strReturnedAddress.toString())
            } else {
                Log.d("address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("address", "Canont get Address!")
        }
        return strAdd
    }

    override fun onResume() {
        super.onResume()
        if (SharedVariable.lokasiPenyedia.latitude != 0.0){
            latlon = ""+SharedVariable.lokasiPenyedia.latitude+","+SharedVariable.lokasiPenyedia.longitude
            alamat = getCompleteAddressString(SharedVariable.lokasiPenyedia.latitude,SharedVariable.lokasiPenyedia.longitude).toString()
            edAlamat.setText(alamat)
        }
    }
}
