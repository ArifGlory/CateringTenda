package com.tapisdev.cateringtenda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tapisdev.cateringtenda.activity.RegisterActivity
import com.tapisdev.cateringtenda.activity.SplashActivity
import com.tapisdev.cateringtenda.base.BaseActivity
import com.tapisdev.cateringtenda.model.UserModel
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    var TAG_LOGIN = "login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mUserPref = UserPreference(this)

        tvSignup.setOnClickListener{
            val i = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(i)
        }
        tvLogin.setOnClickListener {
            signIn()
        }
    }

    fun signIn(){
        var getEmail = edEmail.text.toString()
        var getPass  = edPassword.text.toString()

        if (getEmail.equals("") || getEmail.length == 0){
            showErrorMessage("Email harus diisi")
        }else if (getPass.equals("") || getPass.length == 0){
            showErrorMessage("Password harus diisi")
        }else{
            showLoading(this)
            auth.signInWithEmailAndPassword(getEmail,getPass).addOnCompleteListener(this, OnCompleteListener { task ->
                if(task.isSuccessful){
                    var userId = auth.currentUser?.uid
                    Log.d(TAG_LOGIN,"user ID : "+userId)

                    checkIfEmailVerified()
                }else{
                    dismissLoading()
                    showErrorMessage("Password / Email salah")
                }
            })
        }

    }

    private fun checkIfEmailVerified() {
        val user = FirebaseAuth.getInstance().currentUser
        var userId = auth.currentUser?.uid
        if (user!!.isEmailVerified) {

            userId?.let {
                userRef.document(it).get().addOnCompleteListener{ task ->
                    dismissLoading()
                    if (task.isSuccessful){
                        val document = task.result
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG_LOGIN, "DocumentSnapshot data: " + document.data)
                                //convert doc to object
                                var userModel : UserModel = document.toObject(UserModel::class.java)!!
                                Log.d(TAG_LOGIN,"usermodel name : "+userModel.name)
                                setSession(userModel)

                                val i = Intent(applicationContext, SplashActivity::class.java)
                                startActivity(i)
                            } else {
                                Log.d(TAG_LOGIN, "No such document")
                            }
                        }
                    }else{
                        showErrorMessage("Error saaat mencari di database")
                        Log.d(TAG_LOGIN,"err : "+task.exception)
                    }
                }
            }

        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            dismissLoading()
            showErrorMessage("Anda belum melakukan verifikasi pendaftaran di email")
            FirebaseAuth.getInstance().signOut()
            //restart this activity
        }
    }

    fun setSession(userModel: UserModel){
        mUserPref.saveName(userModel.name)
        mUserPref.saveEmail(userModel.email)
        mUserPref.saveFoto(userModel.foto)
        mUserPref.saveJenisUser(userModel.jenis)
        mUserPref.savePhone(userModel.phone)
        mUserPref.saveAlamat(userModel.alamat)
        mUserPref.saveLatlon(userModel.latlon)
        mUserPref.saveDeskripsi(userModel.deskripsi)
    }
}
