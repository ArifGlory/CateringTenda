package com.tapisdev.cateringtenda.base

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import es.dmoral.toasty.Toasty

open class BaseActivity : AppCompatActivity() {

    lateinit var pDialogLoading : SweetAlertDialog

    override fun setContentView(view: View?) {
        super.setContentView(view)

        pDialogLoading = SweetAlertDialog(applicationContext, SweetAlertDialog.PROGRESS_TYPE)
        pDialogLoading.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialogLoading.setTitleText("Loading..")
        pDialogLoading.setCancelable(false)
    }

    open fun showLoading(){
        pDialogLoading.show()
    }

    fun dismissLoading(){
        pDialogLoading.dismiss()
    }

    fun showErrorMessage(message : String){
        SweetAlertDialog(applicationContext, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(message)
            .show()
    }

    fun showLongErrorMessage(message : String){
        applicationContext?.let { Toasty.error(it, message, Toast.LENGTH_LONG, true).show() }
    }

    fun showInfoMessage(message : String){
        applicationContext?.let { Toasty.info(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showWarningMessage(message : String){
        applicationContext?.let { Toasty.warning(it, message, Toast.LENGTH_SHORT, true).show() }
    }
}