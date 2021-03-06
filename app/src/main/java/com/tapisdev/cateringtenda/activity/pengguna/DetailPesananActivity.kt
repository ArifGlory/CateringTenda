package com.tapisdev.cateringtenda.activity.pengguna

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tapisdev.cateringPesanan.adapter.AdapterPesananUser
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.adapter.AdapterDetailPesanan
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.*
import com.tapisdev.cateringtenda.util.PermissionHelper
import kotlinx.android.synthetic.main.activity_add_catering.*
import kotlinx.android.synthetic.main.activity_detail_pesanan.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DetailPesananActivity : BaseActivity(),PermissionHelper.PermissionListener {

    lateinit var i : Intent
    lateinit var pesanan : Pesanan
    var listCart = ArrayList<Cart>()
    lateinit var adapter: AdapterDetailPesanan
    lateinit var ivBuktiBayar : ImageView
    lateinit var penyedia : UserModel

    var TAG_GET_DETAILPESANAN = "detailpesananGET"
    var TAG_SIMPAN = "saveBukti"
    var TAG_GET_USER = "penyediaGET"
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var  permissionHelper : PermissionHelper
    var fotoBitmap : Bitmap? = null
    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan)
        mUserPref = UserPreference(this)

        i = intent
        pesanan = i.getSerializableExtra("pesanan") as Pesanan

        adapter = AdapterDetailPesanan(listCart)
        rvPesanan.setHasFixedSize(true)
        rvPesanan.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        rvPesanan.adapter = adapter

        tvTanggal.setText("Tanggal pesan : "+pesanan.tanggalPesan?.let { convertDate(it) })
        tvAlamat.setText("Alamat : "+pesanan.alamat)
        tvStatus.setText(pesanan.status)

        storageReference = FirebaseStorage.getInstance().reference.child("images")

        permissionHelper = PermissionHelper(this)
        permissionHelper.setPermissionListener(this)

        ivShowBuktiBayar.setOnClickListener {
            showDialogBuktiBayar()
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvDeskripsiPenyedia.setText("Kode Pesanan : "+pesanan.pesananId?.take(8)!!.toUpperCase())

        getDataPesanan()
        getDataPenyedia()
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
                ivBuktiBayar.setImageBitmap(bitmap)
                uploadBuktiBayar()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getDataPesanan(){
        detailpesananRef.get().addOnSuccessListener { result ->
            listCart.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var cart : Cart = document.toObject(Cart::class.java)
                if (cart.idPesanan.equals(pesanan.pesananId)){
                    listCart.add(cart)
                }
            }
            countTotalPrice()
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_DETAILPESANAN,"err : "+exception.message)
        }
    }

    fun getDataPenyedia(){
        userRef.document(pesanan.idAdmin.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val document = task.result
                if (document != null) {
                    if (document.exists()) {
                        Log.d(TAG_GET_USER, "DocumentSnapshot data: " + document.data)
                        //convert doc to object
                        penyedia = document.toObject(UserModel::class.java)!!
                        //tvDeskripsiPenyedia.setText(penyedia.name+ "\n "+penyedia.deskripsi)

                    } else {
                        Log.d(TAG_GET_USER, "No such document")
                    }
                }
            }else{
                showErrorMessage("Pengguna tidak ditemukan")
                Log.d(TAG_GET_USER,"err : "+task.exception)
            }
        }
    }

    fun countTotalPrice(){
        var totalPrice = 0
        for (c in 0 until listCart.size){
            var subtotal = listCart.get(c).harga?.times(listCart.get(c).jumlah!!)
            var status = listCart.get(c).status
            if (subtotal != null && !status.equals("tolak")) {
                totalPrice += subtotal
            }
        }
        tvTotalPrice.setText("Total Bayar : Rp. "+convertRibuan(totalPrice))
    }

    fun showDialogBuktiBayar(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dlg_bukti_bayar)
        ivBuktiBayar = dialog.findViewById(R.id.ivBuktiBayar) as ImageView
        val tvClose = dialog.findViewById(R.id.tvClose) as TextView
        val tvNorek = dialog.findViewById(R.id.tvNorek) as TextView
        val tvSimpan = dialog.findViewById(R.id.tvSimpan) as TextView

        tvNorek.setText(penyedia.deskripsi)
        if(pesanan.buktiBayar.equals("")){
            ivBuktiBayar.setImageResource(R.drawable.ic_placeholder)
        }else{
            Glide.with(this)
                .load(pesanan.buktiBayar)
                .into(ivBuktiBayar)
        }

        tvClose.setOnClickListener {
            dialog.dismiss()
        }
        ivBuktiBayar.setOnClickListener {
            if (mUserPref.getJenisUser().equals("pengguna")){
                launchGallery()
            }
        }
        dialog.show()
    }

    fun uploadBuktiBayar(){
        showLoading(this)

        if (fileUri != null){
            Log.d(TAG_SIMPAN,"uri :"+fileUri.toString())

            val baos = ByteArrayOutputStream()
            fotoBitmap?.compress(Bitmap.CompressFormat.JPEG,50,baos)
            val data: ByteArray = baos.toByteArray()

            val fileReference = storageReference!!.child(System.currentTimeMillis().toString())
            val uploadTask = fileReference.putBytes(data)

            uploadTask.addOnFailureListener {
                    exception -> Log.d(TAG_SIMPAN, exception.toString())
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
                        Log.d(TAG_SIMPAN,"download URL : "+ downloadUri.toString())// This is the one you should store

                        saveBuktiBayar(url)
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

    fun saveBuktiBayar(url : String){
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy-MM-dd HH:mm:ss")

        pDialogLoading.setTitleText("menyimpan data..")
        showInfoMessage("Sedang menyimpan ke database..")

        //simpan ke tabel notifikasi
        var idNotifikasi = UUID.randomUUID().toString()
        var tanggalpesan = dateToString(pesanan.tanggalPesan!!)
        var notifikasi  = NotifikasiPembayaran(
            idNotifikasi,
            ""+mUserPref.getName()+" telah mengunggah bukti pembayaran",
            mUserPref.getName(),
            dateInString,
            auth.currentUser?.uid,
            pesanan.pesananId,
            pesanan.idAdmin
        )
        notifikasiRef.document(idNotifikasi).set(notifikasi)

        pesanananRef.document(pesanan.pesananId.toString()).update("buktiBayar",url).addOnCompleteListener { task ->
            dismissLoading()
            if (task.isSuccessful){
                pesanan.buktiBayar = url
                showSuccessMessage("Data bukti bayar berhasil ditambahkan")
                //onBackPressed()
            }else{
                showLongErrorMessage("Penyimpanan data gagal")
                Log.d(TAG_SIMPAN,"err : "+task.exception)
            }
        }
    }

    fun dateToString(tanggal : String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val output = formatter.format(parser.parse(tanggal))

        return output
    }

    override fun onPermissionCheckDone() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
