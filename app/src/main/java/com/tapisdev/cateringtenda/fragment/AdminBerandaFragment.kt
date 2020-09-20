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
import com.tapisdev.cateringtenda.adapter.AdapterTenda
import com.tapisdev.cateringtenda.base.BaseFragment
import com.tapisdev.cateringtenda.model.Catering
import com.tapisdev.cateringtenda.model.Tenda
import com.tapisdev.cateringtenda.model.UserPreference
import kotlinx.android.synthetic.main.fragment_admin_tenda.*

class AdminBerandaFragment : BaseFragment() {

    lateinit var rvCatering: RecyclerView
    var TAG_GET_CATERING = "getCatering"
    lateinit var adapter:AdapterCatering
    var listCatering = ArrayList<Catering>()

    lateinit var rvTenda: RecyclerView
    var TAG_GET_TENDA = "getTenda"
    lateinit var adapterTenda: AdapterTenda
    var listTenda = ArrayList<Tenda>()

    lateinit var tvNamaPenyedia : TextView
    lateinit var tvAlamat : TextView
    lateinit var tvTelepon : TextView
    lateinit var tvTitleCatering : TextView
    lateinit var tvTitleTenda : TextView
    lateinit var tvDeskripsi : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_admin_beranda, container, false)

        tvNamaPenyedia = root.findViewById(R.id.tvNamaPenyedia)
        tvAlamat = root.findViewById(R.id.tvAlamat)
        tvTelepon = root.findViewById(R.id.tvTelepon)
        tvTitleCatering = root.findViewById(R.id.tvTitleCatering)
        tvTitleTenda = root.findViewById(R.id.tvTitleTenda)
        tvDeskripsi = root.findViewById(R.id.tvDeskripsi)
        rvCatering = root.findViewById(R.id.rvCatering)
        rvTenda = root.findViewById(R.id.rvTenda)
        mUserPref = UserPreference(requireContext())

        adapter = AdapterCatering(listCatering)
        adapterTenda = AdapterTenda(listTenda)

        rvCatering.setHasFixedSize(true)
        rvCatering.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL ,false)
        rvCatering.adapter = adapter

        rvTenda.setHasFixedSize(true)
        rvTenda.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL ,false)
        rvTenda.adapter = adapterTenda

        updateUI()
        getDataMyCatering()
        getDataMyTenda()
        return root
    }

    fun updateUI(){
        tvNamaPenyedia.setText(mUserPref.getName())
        tvAlamat.setText("Alamat : "+mUserPref.getAlamat())
        tvTelepon.setText("Kontak : "+mUserPref.getPhone())
        tvDeskripsi.setText("Deskripsi : "+mUserPref.getDeskripsi())
    }

    fun getDataMyCatering(){
        cateringRef.get().addOnSuccessListener { result ->
            listCatering.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                //Log.d(TAG_GET_CATERING, "Datanya : "+document.data)
                var catering : Catering = document.toObject(Catering::class.java)
                catering.cateringId = document.id
                if (catering.idAdmin.equals(auth.currentUser?.uid)){
                    listCatering.add(catering)
                }
            }
            if (listCatering.size == 0){
                tvTitleCatering.setText("Belum ada data catering")
            }
            adapter.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_CATERING,"err : "+exception.message)
        }
    }

    fun getDataMyTenda(){
        tendaRef.get().addOnSuccessListener { result ->
            listTenda.clear()
            //Log.d(TAG_GET_CATERING," datanya "+result.documents)
            for (document in result){
                Log.d(TAG_GET_TENDA, "Datanya : "+document.data)

                var tenda : Tenda = document.toObject(Tenda::class.java)
                tenda.tendaId = document.id
                if (tenda.idAdmin.equals(auth.currentUser?.uid)){
                    listTenda.add(tenda)
                }
            }
            if (listTenda.size == 0){
                tvTitleTenda.setText("Belum ada data alat pesta")
            }
            adapterTenda.notifyDataSetChanged()

        }.addOnFailureListener { exception ->
            showErrorMessage("terjadi kesalahan : "+exception.message)
            Log.d(TAG_GET_TENDA,"err : "+exception.message)
        }
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
        getDataMyCatering()
        getDataMyTenda()
    }

}
