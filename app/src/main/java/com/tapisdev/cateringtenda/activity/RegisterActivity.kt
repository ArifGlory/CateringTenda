package com.tapisdev.cateringtenda.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.cateringtenda.MainActivity
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.UserModel
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity() {

    var selectedJenisUser = "none"
    var TAG_JENIS = "jenisUser"
    var TAG_SIMPAN = "simpanUser"
    lateinit var userModel : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mUserPref = UserPreference(this)

        tvLogin.setOnClickListener {
            val i = Intent(applicationContext,MainActivity::class.java)
            startActivity(i)
        }

        spJenisUser.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.d(TAG_JENIS, "jenis nya "+ parent?.getItemAtPosition(position).toString())
                var selected = parent?.getItemAtPosition(position).toString()
                if (selected.equals("Pilih Jenis Pengguna")){
                    selectedJenisUser = "none"
                }else if (selected.equals("Admin Catering/Tenda")){
                    selectedJenisUser = "admin"
                }else if (selected.equals("Pengguna")){
                    selectedJenisUser = "pengguna"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        tvSignup.setOnClickListener {
            checkValidation()
        }

    }

    fun checkValidation(){
        var getName = edFullName.text.toString()
        var getEmail = edEmail.text.toString()
        var getPhone = edMobile.text.toString()
        var getPassword = edPassword.text.toString()
        var getConfirmPassword = edCPassword.text.toString()

        if (getName.equals("") || getName.length == 0){
            showErrorMessage("Nama Belum diisi")
        }else  if (getEmail.equals("") || getEmail.length == 0){
            showErrorMessage("email Belum diisi")
        }else  if (getPhone.equals("") || getPhone.length == 0){
            showErrorMessage("phone Belum diisi")
        }else  if (getPassword.equals("") || getEmail.length == 0){
            showErrorMessage("password Belum diisi")
        }else  if (getConfirmPassword.equals("") || getConfirmPassword.length == 0){
            showErrorMessage("konfirmasi passsword Belum diisi")
        }else if (selectedJenisUser.equals("none")){
            showErrorMessage("Jenis Pengguna Belum dipilih")
        }else if (!getPassword.equals(getConfirmPassword)){
            showErrorMessage("Konfirmasi password tidak valid")
        }else if(getPassword.length < 8 || getConfirmPassword.length < 8){
            showErrorMessage("Password harus lebih dari 8 karakter")
        }
        else{
            userModel = UserModel(getName,getEmail,"",getPhone,selectedJenisUser,"","none","none","")
            Log.d(TAG_SIMPAN," namanya : "+userModel.name)

            registerUser(userModel.email,getPassword)
        }
    }

    fun registerUser(email : String,pass : String){
        showLoading(this)
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, OnCompleteListener{task ->
            if (task.isSuccessful){
                var userId = auth.currentUser?.uid

                if (userId != null) {
                    userModel.uId = userId
                    userRef.document(userId).set(userModel).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            sendVerificationEmail()

                        }else{
                            dismissLoading()
                            showLongErrorMessage("Error pendaftaran, coba lagi nanti ")
                            Log.d(TAG_SIMPAN,"err : "+task.exception)
                        }
                    }
                }else{
                    showLongErrorMessage("user id tidak di dapatkan")
                }
            }else{
                dismissLoading()
                if(task.exception?.equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")!!){
                    showLongErrorMessage("Email sudah pernah digunakan ")
                }else{
                    showLongErrorMessage("Error pendaftaran, Cek apakah email sudah pernah digunakan / belum dan  coba lagi nanti ")
                    Log.d(TAG_SIMPAN,"err : "+task.exception)
                }

            }
        })
    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { // email sent
                    // after email is sent just logout the user and finish this activity
                    FirebaseAuth.getInstance().signOut()
                    dismissLoading()

                    showLongSuccessMessage("Pendaftaran Berhasil, Silakan Verifikasi Email Anda")
                    val i = Intent(applicationContext,MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    dismissLoading()
                    showErrorMessage("Error pengiriman verifikasi email")
                    Log.d("sendEmail","err : "+task.exception)
                }
            }
    }
}

