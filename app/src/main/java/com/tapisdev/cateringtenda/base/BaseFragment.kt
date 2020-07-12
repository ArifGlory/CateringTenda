package com.tapisdev.cateringtenda.base

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import es.dmoral.toasty.Toasty

open class BaseFragment : Fragment() {

    lateinit var pDialogLoading : SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pDialogLoading = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
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
        SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText(message)
            .show()
    }

    fun showLongErrorMessage(message : String){
        activity?.let { Toasty.error(it, message, Toast.LENGTH_LONG, true).show() }
    }

    fun showInfoMessage(message : String){
        activity?.let { Toasty.info(it, message, Toast.LENGTH_SHORT, true).show() }
    }

    fun showWarningMessage(message : String){
        activity?.let { Toasty.warning(it, message, Toast.LENGTH_SHORT, true).show() }
    }
}

