package com.tapisdev.cateringtenda.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.makeramen.roundedimageview.RoundedImageView
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.UserPreference
import com.tapisdev.cateringtenda.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_add_catering.*
import kotlinx.android.synthetic.main.fragment_admin_tenda.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList

class UserProfilFragment : BaseFragment(),PermissionHelper.PermissionListener {

    lateinit var rvCatering: RecyclerView
    lateinit var tvLogout : TextView
    lateinit var tvEnableUpdate : TextView
    lateinit var tvSaveUpdate : TextView
    lateinit var edUserName : EditText
    lateinit var edMobileNumber : EditText
    lateinit var ivProfile : RoundedImageView
    lateinit var ivGallery : ImageView
    var state = "view"

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null
    private var TAG_SIMPAN_FOTO = "simpanFoto"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_user_profil, container, false)
        mUserPref = UserPreference(this.requireActivity())
        storageReference = FirebaseStorage.getInstance().reference.child("images")

        tvLogout = root.findViewById(R.id.tvLogout)
        tvEnableUpdate = root.findViewById(R.id.tvEnableUpdate)
        tvSaveUpdate = root.findViewById(R.id.tvSaveUpdate)
        edUserName = root.findViewById(R.id.edUserName)
        edMobileNumber = root.findViewById(R.id.edMobileNumber)
        ivProfile = root.findViewById(R.id.ivProfile)
        ivGallery = root.findViewById(R.id.ivGallery)

        permissionHelper = PermissionHelper(activity)
        permissionHelper.setPermissionListener(this)


        tvLogout.setOnClickListener {
            auth.signOut()
            clearSession()
            val i = Intent(activity, MainActivity::class.java)
            startActivity(i)
        }
        ivGallery.setOnClickListener { 
            launchGallery()
        }
        tvEnableUpdate.setOnClickListener {
            state = "edit"
            updateUI()
        }
        tvSaveUpdate.setOnClickListener {
            checkValidation()
        }

        updateUI()
        return root
    }

    fun checkValidation(){
        var getName = edUserName.text.toString()
        var getMobileNumber = edMobileNumber.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        } else if (getMobileNumber.equals("") || getMobileNumber.length == 0){
            showErrorMessage("No. Handphone Belum diisi")
        }else {
            showLoading(activity)
            userRef.document(auth.currentUser?.uid.toString()).update("name",getName)
            userRef.document(auth.currentUser?.uid.toString()).update("phone",getMobileNumber).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    dismissLoading()
                    edMobileNumber.setText(getMobileNumber)
                    edUserName.setText(getName)
                    state = "view"

                    mUserPref.saveName(getName)
                    mUserPref.savePhone(getMobileNumber)

                    updateUI()
                    showSuccessMessage("Data berhasil diubah")
                }else{
                    dismissLoading()
                    showLongErrorMessage("Penyimpanan data gagal")
                    Log.d(TAG_SIMPAN_FOTO,"err : "+task.exception)
                }
            }
        }
    }

    private fun launchGallery() {
        var listPermissions: MutableList<String> = ArrayList()
        listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionHelper.checkAndRequestPermissions(listPermissions)
    }

    companion object {
        fun newInstance(): UserProfilFragment{
            val fragment = UserProfilFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
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
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                fotoBitmap = bitmap
                uploadFotoUser()
                //ivCatering.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    fun updateUI(){
        if (state.equals("view")){
            edUserName.setText(mUserPref.getName())
            edMobileNumber.setText(mUserPref.getPhone())
            tvSaveUpdate.visibility = View.INVISIBLE


            if (!mUserPref.getFoto().equals("")){
                Glide.with(requireActivity())
                    .load(mUserPref.getFoto())
                    .into(ivProfile)
            }
            tvEnableUpdate.visibility = View.VISIBLE

            edUserName.isEnabled = false
            edMobileNumber.isEnabled = false
        }else if (state.equals("edit")){
            edUserName.isEnabled = true
            edMobileNumber.isEnabled = true

            edUserName.background = resources.getDrawable(R.drawable.bg_onlycorner_gary)
            edMobileNumber.background = resources.getDrawable(R.drawable.bg_onlycorner_gary)

            tvEnableUpdate.visibility = View.INVISIBLE
            tvSaveUpdate.visibility = View.VISIBLE

            tvSaveUpdate.isEnabled = true
        }

    }

    fun clearSession(){
        mUserPref.saveName("")
        mUserPref.saveEmail("")
        mUserPref.saveFoto("")
        mUserPref.saveJenisUser("none")
        mUserPref.savePhone("")
    }

    override fun onPermissionCheckDone() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    fun uploadFotoUser(){
        showLoading(activity)

        if (fileUri != null){
            Log.d(TAG_SIMPAN_FOTO,"uri :"+fileUri.toString())

            val baos = ByteArrayOutputStream()
            fotoBitmap?.compress(Bitmap.CompressFormat.JPEG,50,baos)
            val data: ByteArray = baos.toByteArray()

            val fileReference = storageReference!!.child(System.currentTimeMillis().toString())
            val uploadTask = fileReference.putBytes(data)

            uploadTask.addOnFailureListener {
                    exception -> Log.d(TAG_SIMPAN_FOTO, exception.toString())
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

                        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAu.getInstance().getCurrentUser().getUid());
                        val url = downloadUri!!.toString()
                        Log.d(TAG_SIMPAN_FOTO,"download URL : "+ downloadUri.toString())// This is the one you should store
                        saveFotoUser(url)

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
            showErrorMessage("Anda belum memilih file")
        }

    }

    fun saveFotoUser(urlFoto : String){
        pDialogLoading.setTitleText("menyimpan data..")
        showInfoMessage("Sedang menyimpan ke database..")

        userRef.document(auth.currentUser?.uid.toString()).update("foto",urlFoto).addOnCompleteListener { task ->
            if (task.isSuccessful){
                dismissLoading()
                ivProfile.setImageBitmap(fotoBitmap)
                mUserPref.saveFoto(urlFoto)

                showSuccessMessage("Foto berhasil diubah")
            }else{
                dismissLoading()
                showLongErrorMessage("Penyimpanan data gagal")
                Log.d(TAG_SIMPAN_FOTO,"err : "+task.exception)
            }
        }
    }

}
