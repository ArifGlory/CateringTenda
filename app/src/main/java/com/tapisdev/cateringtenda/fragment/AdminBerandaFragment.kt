package com.tapisdev.cateringtenda.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tapisdev.cateringtenda.R
import com.tapisdev.cateringtenda.activity.admin.AddCateringActivity
import com.tapisdev.cateringtenda.adapter.AdapterCatering
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class AdminBerandaFragment : BaseFragment() {

    lateinit var rvCatering: RecyclerView
    lateinit var fab : FloatingActionButton
    var TAG_GET_CATERING = "getCatering"
    lateinit var adapter:AdapterCatering

    var listCatering = ArrayList<Catering>()

    lateinit var tvNamaPenyedia : TextView
    lateinit var tvAlamat : TextView
    lateinit var tvTelepon : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_beranda, container, false)

        tvNamaPenyedia = root.findViewById(R.id.tvNamaPenyedia)
        tvAlamat = root.findViewById(R.id.tvAlamat)
        tvTelepon = root.findViewById(R.id.tvTelepon)
        mUserPref = UserPreference(requireContext())

        updateUI()
        return root
    }

    fun updateUI(){
        tvNamaPenyedia.setText(mUserPref.getName())
        tvAlamat.setText("Alamat : "+mUserPref.getAlamat())
        tvTelepon.setText("Kontak : "+mUserPref.getPhone())
    }

    companion object {
        fun newInstance(): AdminBerandaFragment{
            val fragment = AdminBerandaFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onResume() {
        super.onResume()
    }

}
